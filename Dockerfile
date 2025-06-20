# JDK 21 기반 공식 이미지 사용
FROM openjdk:21-jdk-slim

# 컨테이너 내 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일을 컨테이너에 복사
COPY build/libs/be-airdnd-0.0.1-SNAPSHOT.jar be-airdnd.jar

# (필요시 포트 노출)
EXPOSE 8080

# 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "/app/be-airdnd.jar"]
