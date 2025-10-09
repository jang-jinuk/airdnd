# AirDnD

## 📝 Index

1. [Overview](#-overview)
2. [Skill Stack](#-skill-stack)
3. [ERD](#-erd)
4. [Server Architecture](#-server-architecture)
5. [CI/CD](#-ci-cd)
6. [Trouble Shooting](#-trouble-shooting)

## 📖 Overview

**프로젝트 소개**

- AirDnD는 숙소 예약 플랫폼으로, 사용자가 숙소를 검색하고 예약할 수 있습니다.
- 실제 Airbnb 서비스를 참고하여 핵심 예약 기능을 구현한 클론 코딩 프로젝트입니다.

**개발 기간**

- 2025.06.10 ~ 2025.06.27

**개발 인원**

- 백엔드 3명

## 🛠️ Skill Stack

<img src="https://mudhub-bucket.s3.ap-northeast-2.amazonaws.com/gist/Skill.png">

## 🗂️ ERD

<img src="https://mudhub-bucket.s3.ap-northeast-2.amazonaws.com/gist/Screenshot+2025-08-14+at+3.55.58%E2%80%AFPM.png" width="600">

## 🖥️ Server Architecture

<img src="https://mudhub-bucket.s3.ap-northeast-2.amazonaws.com/gist/AWS+cloud+diagram.png" height="400">

설계 가이드

- 서버의 부하를 분산시키고, 서버의 안정성을 높이기 위해 백엔드와 프론트엔드 서버를 분리한 서버 아키텍처를 구성했습니다.
- 백엔드 서버의 EC2 인스턴스에 Nginx, Spring Boot 서버를 Docker 컨테이너로 배포하여 개발환경과 서버환경의 제약을 받지 않고 안전하게 운영되도록 구성했습니다.
- 데이터베이스(MySQL, Redis)의 경우 EC2 인스턴스와 동일한 VPC 내에 생성하되 Private Subnet으로 분리하고, 데이터베이스 포트는 EC2 인스턴스에서만 접속할 수 있도록 하여 외부에서 직접
  접근하지 못하게 함으로써 보안적으로 안전한 환경을 구성했습니다.

## 🔄 CI/CD

- Github Actions, Docker Hub, 셸 스크립트를 사용하여 CI/CD 파이프 라인을 구축했습니다.
- dev 브랜치에 변경사항이 push 되면, `deploy-docker.yml`[(파일
  링크)](https://github.com/jang-jinuk/be-airdnd/blob/dev/.github/workflows/deploy-docker.yml) 에 작성된 워크 플로우에 따라 Github
  Actions가 동작합니다.
- 민간한 정보(Docker Hub id/pw, Ec2 ip주소, SSH key)는 `Github Secrets`로 관리했습니다.
- Nginx와 Docker를 기반으로 **Blue-Green** 배포 전략을 적용하여 서비스 중단 없이 신규 버전을 배포할 수 있는 **무중단 배포** 시스템을 구축했습니다.

### 동작 순서

**1. 빌드**

- `actions/checkout`를 통해 러너 환경에 레포지토리 소스 코드 클론
- JDK를 설치하여, java 빌드 환경 설정
- `./gradlew clean build`를 통해 java 소스 코드를 빌드(`.jar` 파일 생성)

**2. Docker 이미지 작업**

- `Docker Hub`에 로그인
- 프로젝트에 `Dockerfile`을 통해 빌드하여 도커 이미지 파일 생성
- 생성된 도커 이미지 파일은 `Docker Hub`에 `push`

**3. EC2에 배포**

- `SSH`를 통해 EC2 서버 접속
- `deploy.sh`[(파일 링크)](https://github.com/jang-jinuk/be-airdnd/blob/dev/deploy.sh) 셸 스트립트 실행
- 현재 `nginx` 설정 파일에서 active 중인 컨테이너를 찾음
- `Docker Hub`에서 최신 이미지 `pull`
- 새 컨테이너 실행 (`.env` 파일 포함)
- 새 컨테이너가 정상 실행 되었는지 `헬스체크`
- nginx 설정 변경 (트래픽 전환)
- `nginx -s reload`를 통해 nginx 리로드
- 이전 컨테이너 제거

## ☄️ Trouble Shooting

**1. 숙소 검색 API 성능 개선: 160배 속도 향상(10초 → 0.062초)**

- **공간 데이터 계산 함수(Spatial Function)** 가 포함된 쿼리로 인해, 숙소 검색 API의 응답 속도가 10초 이상 소요되는 성능 저하 발생
- 쿼리 실행 계획 분석을 통해 비효율적인 부분을 파악하고,**복합 인덱스 및 공간 인덱스**를 적용하여 쿼리 성능 최적화
- API 응답 속도를 **0.062초**로 단축하며 **약 160배의 성능 개선** 달성
- 관련 내용 블로그 [(해당링크)](https://mudhub.tistory.com/6)

**2. N+1 문제 해결: 불필요한 쿼리 95% 감소 (62개 → 3개)**

- 숙소 목록 조회 시 연관된 여러 엔티티를 조회하는 과정에서 **N+1** 문제가 발생하여, **62개의 쿼리가 실행**되고 **3초** 이상의 응답 지연 유발
- **Fetch Join**을 사용해 즉시 로딩(Eager Loading)을 적용하고, 1:N 관계에서 발생하는 데이터 중복 및 페이징 문제 해결을 위해 **@BatchSize** 옵션 병용
- 실행 쿼리 수를 **3개**로 최적화하여 API 응답 속도 개선 및 DB 부하 감소
- 관련 내용 블로그 [(해당링크)](https://mudhub.tistory.com/3)

**3. 데이터 정합성 확보: 트랜잭션 분리를 통한 안정성 강화**

- 숙소 예약 API 실행 중 트랜잭션 커밋 지연으로 데이터 정합성 이슈 발생
- **REQUERS_NEW** 전파 속성으로 트랜잭션 분리
- **1000 TPS**의 부하 테스트 환경에서도 모든 요청에 대한 데이터 정합성을 **100%** 보장
- 관련 내용 블로그 [(해당 링크)](https://mudhub.tistory.com/2)

**4. 분산 환경 동시성 제어: Redis 분산 락 도입**

- 단일 서버 환경에서는 비관적 락을 활용해 중복 예약 방지
- 서버 확장(다중 서버 운영) 시 DB 락 한계로 인해 Redis 분산 락 도입
- Redis 분산 락을 통해 다중 서버 환경에서도 안정적으로 예약 동시성 문제 해결




