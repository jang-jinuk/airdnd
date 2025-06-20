#!/bin/bash

# 0. 락 획득 (한 번에 하나의 프로세스만 동작)
exec 200>/var/lock/deploy.lock
flock -n 200 || { echo "[ERROR] 이미 배포 스크립트 실행 중. 종료합니다."; exit 1; }

# 1. 현재 nginx 설정에서 active 대상 읽기
CURRENT=$(grep 'server app' ./nginx/conf.d/default.conf | grep -o 'app[a-z]*')

# 2. 반대쪽 컨테이너를 배포 대상으로 선택
if [[ "$CURRENT" == "appblue" ]]; then
  TARGET="appgreen"
else
  TARGET="appblue"
fi

echo "[INFO] 현재 nginx는 $CURRENT 사용 중 → $TARGET 배포 시작"

# 3. 최신 이미지 pull
echo "[INFO] Docker 이미지 pull 중..."
docker pull jdagon2000/be-airdnd:latest

# 4. 기존 컨테이너 중 대상이 뜨면 중지 및 제거
docker rm -f $TARGET 2>/dev/null || true

# 5. 새 컨테이너 실행 (env 파일 포함)
docker run -d \
  --name $TARGET \
  --network webnet \
  --env-file .env \
  jdagon2000/be-airdnd:latest

# 6. 헬스체크
echo "[INFO] 헬스체크 중..."
for i in {1..10}; do
  sleep 5
  if docker exec nginx curl -s http://$TARGET:8080/api/health | grep -q OK;
  then
    echo "[OK] $TARGET 헬스체크 통과"
    break
  fi
  echo -n "."
  if [[ $i -eq 10 ]]; then
    echo "[ERROR] 헬스체크 실패. 배포 중단"
    # 실패한 컨테이너 정리
    echo "[INFO] 실패한 컨테이너 $TARGET 제거"
    docker rm -f $TARGET
    exit 1
  fi
done

# 7. nginx 설정 변경
echo "[INFO] nginx 설정 변경 → $TARGET 사용"
sed -i "s/server app.*:8080;/server ${TARGET}:8080;/" ./nginx/conf.d/default.conf

# 8. nginx 리로드
echo "[INFO] nginx 리로드"
docker exec nginx nginx -s reload

echo "[SUCCESS] 무중단 배포 완료: $TARGET"

echo "[INFO] 이전 컨테이너 $CURRENT 종료 및 제거"
docker rm -f $CURRENT
