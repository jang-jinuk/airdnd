# airdnd

## 📝 Index

1. [Overview](#-overview)
2. [Skill Stack](#-skill-stack)
3. [ERD](#-erd)
4. [Server Architecture](#-server-architecture)
5. [CI/CD](#-cicd)
6. [Trouble Shooting](#-trouble-shooting)
7. [Reflection](#-reflection)

## 📖 Overview

**프로젝트 소개**

- airdnd는 숙소 검색·예약 플랫폼입니다.
- 실제 airbnb 서비스를 참고하여 핵심 기능을 구현한 클론 코딩 프로젝트입니다.

**개발 및 개선 기간**

- 개발 기간: 2025.06.10 - 2025.06.27 (백엔드 3명 참여)
- 개선 기간: 2025.08.14 - 2025.10.09 (개인)

**개발 인원**
<div align="center">

|                    [MUD](https://github.com/jang-jinuk)                     |                     [Dino](https://github.com/2jiyong)                      |                   [kaydeen](https://github.com/doopang24)                   |
|:---------------------------------------------------------------------------:|:---------------------------------------------------------------------------:|:---------------------------------------------------------------------------:|
| <img src="https://avatars.githubusercontent.com/u/143267143?v=4" width=180> | <img src="https://avatars.githubusercontent.com/u/164735145?v=4" width=180> | <img src="https://avatars.githubusercontent.com/u/153696777?v=4" width=180> |
|                                   Backend                                   |                                   Backend                                   |                                   Backend                                   |

</div>

## 🛠️ Skill Stack

<img src="https://mudhub-bucket.s3.ap-northeast-2.amazonaws.com/gist/Skill.png">

## 🗂️ ERD

<img src="https://mudhub-bucket.s3.ap-northeast-2.amazonaws.com/images/ERDiagrams.png" width="600">

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

## 🧪 Test Strategy

**코드의 안정성 확보**, **팀의 개발 속도 향상**, 그리고 **지속 가능한 유지보수**를 목표로 다음과 같은 체계적인 테스트 전략을 수립하고 실행했습니다.

**1\. TDD (테스트 주도 개발) 방법론 채택**

개발 초기부터 TDD(Test-Driven Development) 방식을 도입하여, 기능 구현 전에 테스트 코드를 먼저 작성하는 것을 원칙으로 삼았습니다.

- **조기 버그 발견:** 코드를 작성하는 동시에 테스트를 진행함으로써, 구현 단계에서 발생할 수 있는 논리적 오류를 조기에 발견하고 즉시 수정할 수 있었습니다.
- **안전망 확보:** 모든 비즈니스 로직에는 이를 검증하는 테스트 코드가 존재합니다. 이는 **리팩토링에 대한 자신감**으로 이어졌습니다. 내부 구현을 변경하더라도 기존 테스트가 모두
  통과한다면, 기능이 동일하게 동작함을 보장받을 수 있었습니다.

**2\. Testcontainers를 통한 일관된 테스트 환경 구축**

팀원들의 로컬 환경과 실제 배포 환경(CI/CD, 운영 서버) 간의 차이로 인해 발생하는 문제를 원천적으로 차단하고자 **Testcontainers**를 적극 활용했습니다.

- **환경 불일치 해소:** 모든 팀원이 **운영 환경과 동일한 버전의 데이터베이스(예: MySQL, Redis 등) 컨테이너**를 로컬에서 실행하여 테스트를 진행했습니다.
- **배포 안정성 극대화:** "제 PC에서는 됐는데 서버에서는 안 돼요"와 같은 고질적인 환경 문제를 해결했습니다. 개발 환경과 배포 환경의 간극을 최소화함으로써, **코드 통합 및 배포 시 발생할 수 있는
  오류를 크게 줄였습니다.**

**3\. 전략적 선택: 단위 테스트(Unit Test) 집중**

프로젝트의 **빠른 마감 기한**을 준수하는 동시에 품질을 확보하기 위해, 상대적으로 많은 리소스가 필요한 통합 테스트(Integration Test)보다는 **단위 테스트(Unit Test)에 집중**하는 전략을
선택했습니다.

- **신속한 피드백 및 개발 속도 향상:** 단위 테스트는 실행 속도가 빠르고 격리되어 있어, 개발자가 작성한 코드의 결과를 즉각적으로 피드백받을 수 있었습니다. 이는 자연스럽게 **전체적인 개발 속도 향상**에
  기여했습니다.

**4\. Test Data Factory 패턴을 활용한 테스트 코드 품질 관리**

테스트 코드 역시 실제 운영 코드만큼 중요하게 관리되어야 한다는 원칙 아래, **Test Data Factory 패턴**을 도입하여 테스트 코드의 가독성과 유지보수성을 높였습니다.

* **가독성 및 의도 명확화:** 아래 예시와 같이
  `TestAccommodationFactory`[(소스 코드 링크)](https://github.com/jang-jinuk/airdnd/blob/main/src/test/java/com/dmz/airdnd/fixture/TestAccommodationFactory.java)
  같은 팩토리 클래스를 두어, 테스트에 필요한 객체 생성을 위임했습니다.

  ```java
  // 테스트 코드 본문
  Accommodation accommodation = TestAccommodationFactory.createTestAccommodation(1L);
  ```

  이를 통해 테스트 코드 본문은 **'무엇을(Given)', '어떻게(When)', '그래서(Then)'** 검증하는지에만 집중할 수 있어 **테스트의 의도가 명확**하게 드러났습니다.

- **유지보수 용이성:** 도메인 객체의 스펙(필드 추가/삭제 등)이 변경될 경우, 흩어져 있는 모든 테스트 코드를 수정하는 대신 **Test Factory 클래스만 수정**하면 되어 유지보수 비용이 획기적으로
  줄었습니다.

- **협업 효율 증진:** 잘 분리된 Test Data Factory와 명확한 테스트 코드는 그 자체로 **소스코드의 문서** 역할을 했습니다. 팀원들이 서로의 코드를 이해하고 통합할 때 발생할 수 있는 지연
  시간을 줄이는 데 크게 도움이 되었습니다.

## ☄️ Trouble Shooting

**1. 숙소 검색 API 성능 개선: 160배 속도 향상(10초 → 0.062초)**

- **공간 데이터 계산 함수(Spatial Function)** 가 포함된 쿼리로 인해, 숙소 검색 API의 응답 속도가 10초 이상 소요되는 성능 저하 발생
- 쿼리 실행 계획 분석을 통해 비효율적인 부분을 파악하고,**단일 인덱스 및 공간 인덱스**를 적용하여 쿼리 성능 최적화
- API 응답 속도를 **0.062초**로 단축하며 **약 160배의 성능 개선** 달성
- 관련 내용 블로그 [(해당링크)](https://mudhub.tistory.com/6)

**2. N+1 문제 해결: 불필요한 쿼리 95% 감소 (62개 → 3개)**

- 숙소 목록 조회 시 연관된 여러 엔티티를 조회하는 과정에서 **N+1** 문제가 발생하여, **62개의 쿼리가 실행**되고 **3초** 이상의 응답 지연 유발
- **Fetch Join**을 사용해 즉시 로딩(Eager Loading)을 적용하고, 1:N 관계에서 발생하는 데이터 중복 및 페이징 문제 해결을 위해 **@BatchSize** 옵션 병용
- 실행 쿼리 수를 **3개**로 최적화하여 DB 부하 감소
- 관련 내용 블로그 [(해당링크)](https://mudhub.tistory.com/3)

**3. 데이터 정합성 확보: 트랜잭션 분리를 통한 안정성 강화**

- 숙소 예약 API 실행 중 트랜잭션 커밋 지연으로 데이터 정합성 이슈 발생
- **REQUERS_NEW** 전파 속성으로 트랜잭션 분리
- **1000 TPS**의 부하 테스트 환경에서도 모든 요청에 대한 데이터 정합성을 **100%** 보장
- 관련 내용 블로그 [(해당 링크)](https://mudhub.tistory.com/2)

## 💭 Reflection

프로젝트를 진행하며 팀원들과 함께 서비스에 가장 적합한 기술이 무엇일지 심도 있게 논의하고 결정하는 과정이 즐거웠습니다.
혼자 개발할 때보다, 지식을 공유하고 논의하는 과정을 통해 프로젝트의 완성도를 높이는 동시에, 팀과 개인이 함께 성장할 수 있다는 것을 직접 경험했습니다.
이 경험을 통해 협업 시 필요한 효과적인 **소통 능력**과, 프로젝트 완성도를 극대화하기 위해 **다양한 의견을 수렴하고 조율**하는 능력을 향상시킬 수 있는 귀중한 시간이었습니다.

개인적으로 가졌던 성능 개선 기간은 저의 개발 역량을 한 단계 끌어올린 소중한 시간이었습니다.
특히 **N+1 문제, 쿼리 최적화, 트랜젝션 분리** 같은 문제 해결 과정을 통해 데이터베이스 및 프레임워크에 대한 이해를 깊이 할 수 있었습니다.
이 과정을 거치며 기술을 단순히 사용하는 것을 넘어, '**왜 사용해야 하는지**', '**내부적으로 어떻게 동작하는지**'를 이해하는 것이 성능 측면에서 얼마나 큰 차이를 만들어내는지 다시금 깨달았습니다.

또한, 쿼리 최적화를 진행하던 중 QueryDSL 공간 함수 미지원으로 인한 한계를 경험하면서 "**하나의 기술에 맹목적으로 의존하기보다는, 각 기술의 장단점을 명확히 파악하고, 서비스의 요구사항과 성능 목표에 맞춰
적절한 도구를 선택하여 사용해야 한다**"는 중요한 교훈을 얻었습니다.
특정 기술에 대한 의존성을 줄이고 상황에 맞는 유연한 아키텍처를 설계하는 능력이 엔지니어에게 얼마나 중요한지 깨닫게 된 소중한 시간이었습니다.

이번 프로젝트의 경험들을 바탕으로, 저는 기술에 대한 깊은 이해를 지속적으로 확장하며, 언제나 서비스에 **가장 적합한 기술**을 활용해 **최고의 사용자 경험을 제공**하는 성장하는 개발자가 되고싶습니다!
