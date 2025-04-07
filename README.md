<div align="center">
    
# 💰Bankly💰
은행 서비스를 위한 플랫폼

### 📌 서비스 개요
숫자와 금액처럼 민감한 데이터를 다루는 은행 서비스를 구현하며,<br>
**정확성**과 **신뢰성**을 갖춘 플랫폼 개발 역량을 키우고자 이 프로젝트를 진행했습니다.<br>
**실시간 계좌 거래**와 **적금 만기 시 자동 이자 지급** 시스템을 통해 실제 금융 서비스와 유사한 환경을 제공합니다.

### 🛠️ 기술 스택
![Java](https://img.shields.io/badge/Java-21-blue?logo=openjdk&logoColor=white)       
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen?logo=springboot) 
![JPA](https://img.shields.io/badge/JPA-ORM-orange)                                  
![Spring Security](https://img.shields.io/badge/Spring%20Security-auth-green?logo=springsecurity) 
![MySQL](https://img.shields.io/badge/MySQL-8.0.32-blue?logo=mysql&logoColor=white)  
![Redis](https://img.shields.io/badge/Redis-Cache-red?logo=redis&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Message%20Broker-orange?logo=rabbitmq)    
![Docker](https://img.shields.io/badge/Docker-Container%20Platform-2496ED?logo=docker)    
![AWS](https://img.shields.io/badge/AWS-Cloud-232F3E?logo=amazonaws)
![RDS](https://img.shields.io/badge/RDS-Relational%20Database-527FFF?logo=amazonrds)
![Route 53](https://img.shields.io/badge/Route%2053-DNS%20Management-232F3E?logo=amazonroute53)

### ⏰ 개발 기간 및 인원
2024-10-01 ~ 2024-12-15, 1명

</div>


# 📋 목차
1. [서비스 아키텍처 및 시퀀스 다이어그램](#1-서비스-아키텍처-및-시퀀스-다이어그램)  
2. [ERD 및 디렉토리 구조](#2-erd-및-디렉토리-구조)  
3. [주요 기능](#3-주요-기능)  
4. [API 문서](#4-api-문서)  
5. [서비스 및 포트 정보](#5-서비스-및-포트-정보)  
6. [로그 관리](#6-로그-관리)  
7. [주요 기능 화면](#7-주요-기능-화면)  

### 1. 서비스 아키텍처 및 시퀀스 다이어그램

**⚡서비스 아키텍처**<br>
![bankly_서비스아키텍처 (1)](https://github.com/user-attachments/assets/65eca790-ed74-4b71-afe1-8c0df6af6689)


    
**⚡ 시퀀스 다이어그램**   
![bankly-sequenceDiagram](https://github.com/user-attachments/assets/f6ffa73e-f339-49c5-b05d-e54929d020d8)



### 2. ERD 및 디렉토리 구조
**🌈ERD**<br>
![image](https://github.com/user-attachments/assets/b1e316fa-1734-4467-8f03-7875cf1c41d4)

**🌈 디렉터리 구조**<br>
```markdown

java
└─com
    └─project
        └─bankassetor
            ├─ BankAssetApplication.java
            ├─ api
            │   ├─ BankApi.java
            │   └─ HealthCheckApi.java
            ├─ batch
            │   └─ ScheduledBatchConfig.java
            ├─ config
            │   ├─ AppConfig.java
            │   ├─ CachingConfig.java
            │   ├─ PrimaryDBConfig.java
            │   ├─ RabbitMQConfig.java
            │   ├─ SecondaryDBConfig.java
            │   ├─ WebConfig.java
            │   ├─ WebSecurityConfig.java
            │   └─ security
            │       ├─ Authed.java
            │       ├─ CustomUserDetailService.java
            │       ├─ LoginFailHandler.java
            │       ├─ LoginSuccessHandler.java
            │       └─ MyLogoutSuccessHandler.java
            ├─ controller
            │   ├─ AccountsPage.java
            │   ├─ JoinPage.java
            │   ├─ LoginPage.java
            │   └─ MainAdminPage.java
            ├─ exception
            │   ├─ AccessLogException.java
            │   ├─ AccountNotFoundException.java
            │   ├─ BalanceNotEnoughException.java
            │   ├─ BankException.java
            │   ├─ ErrorCode.java
            │   ├─ GlobalExceptionHandler.java
            │   └─ UserNotFoundException.java
            ├─ filter
            │   ├─ AccessLogFilter.java
            │   ├─ AccessLogUtil.java
            │   └─ response
            │       └─ LocationResponse.java
            ├─ listener
            │   └─ AccessLogListener.java
            ├─ primary
            │   ├─ model
            │   │   ├─ entity
            │   │   │   ├─ Account.java
            │   │   │   ├─ BaseEntity.java
            │   │   │   ├─ Config.java
            │   │   │   ├─ Member.java
            │   │   │   └─ account
            │   │   │       ├─ check
            │   │   │       │   ├─ CheckingAccount.java
            │   │   │       │   └─ CheckingTransactionHistory.java
            │   │   │       └─ save
            │   │   │           ├─ SavingAccount.java
            │   │   │           ├─ SavingProduct.java
            │   │   │           └─ SavingTransactionHistory.java
            │   │   ├─ enums
            │   │   │   ├─ AccountStatus.java
            │   │   │   ├─ AccountType.java
            │   │   │   └─ TransactionType.java
            │   │   ├─ request
            │   │   │   ├─ AccountCreateRequest.java
            │   │   │   ├─ AccountRequest.java
            │   │   │   ├─ InterestCalcRequest.java
            │   │   │   ├─ JoinRequest.java
            │   │   │   ├─ SavingAccountCreateRequest.java
            │   │   │   └─ StringMultiValueMapAdapter.java
            │   │   └─ response
            │   │       ├─ AccountCreateResponse.java
            │   │       ├─ AccountResponse.java
            │   │       ├─ AccountTransferResponse.java
            │   │       ├─ CheckingTransactionHistoryResponse.java
            │   │       ├─ DataTableView.java
            │   │       ├─ InterestCalcResponse.java
            │   │       ├─ JoinResponse.java
            │   │       ├─ RestError.java
            │   │       ├─ ResultResponse.java
            │   │       ├─ SavingProductResponse.java
            │   │       ├─ SavingTransactionHistoryResponse.java
            │   │       └─ TerminateResponse.java
            │   └─ repository
            │       ├─ AccountRepository.java
            │       ├─ CheckingAccountRepository.java
            │       ├─ CheckingTransactionHistoryRepository.java
            │       ├─ ConfigRepository.java
            │       ├─ MemberRepository.java
            │       ├─ SavingAccountRepository.java
            │       ├─ SavingProductRepository.java
            │       └─ SavingTransactionHistoryRepository.java
            ├─ secondary
            │   ├─ model
            │   │   └─ entity
            │   │       └─ AccessLog.java
            │   └─ repository
            │       └─ AccessLogRepository.java
            ├─ service
            │   ├─ front
            │   │   └─ BankFrontService.java
            │   └─ perist
            │       ├─ AccessLogService.java
            │       ├─ AccountService.java
            │       ├─ CheckingAccountService.java
            │       ├─ CheckingTransactionHistoryService.java
            │       ├─ ConfigService.java
            │       ├─ InterestCalculationService.java
            │       ├─ MemberService.java
            │       ├─ SavingAccountService.java
            │       ├─ SavingProductService.java
            │       ├─ SavingTransactionHistoryService.java
            │       └─ TelegramNotificationService.java
            └─ utils
                └─ Utils.java

resources
├─ templates
│   ├─ create-account.html
│   ├─ expect-interest.html
│   ├─ index.html
│   ├─ interest-calculate.html
│   ├─ join.html
│   ├─ login.html
│   ├─ my-accounts.html
│   ├─ saving-products-detail.html
│   ├─ saving-products.html
│   ├─ transaction-history.html
│   ├─ transfer.html
└─  └─ layout

test
├─ java
│   └─ com
│       └─ project
│           └─ bankassetor
│               ├─ BankAssetApplicationTests.java
│               ├─ fixture
│               │   └─ MemberFixture.java
│               ├─ listener
│               │   └─ AccessLogListenerTest.java
│               ├─ repository
│               └─ service
│                   └─ perist
│                       ├─ AccountServiceTest.java
│                       ├─ ConfigServiceTest.java
│                       ├─ InterestCalculationServiceTest.java
│                       ├─ MemberServiceTest.java
│                       └─ SavingProductServiceTest.java
└─ resources
    └─ application-test.yml

```



### 3. 주요 기능
- **로그인 및 회원가입**
    - **Spring Security**를 활용해 회원 가입 시 비밀번호 암호화 및 권한 부여 로직 구현, 로그인 기능 설계
    - **JDBC 기반 Session Store**를 통해 세션 데이터를 데이터베이스에 저장하여 사용자 인증 상태를 유지

- **계좌 관리**
    - 당좌 계좌 및 적금 계좌 **생성 및 조회** 기능 구현
    - **Spring MVC** 패턴을 활용해 계좌 간 **입금, 출금, 계좌이체** 기능 개발
    - 계좌별 **거래 내역 조회** 기능 제공

- **엑세스 로그 관리**
    - **RabbitMQ 기반 메시지 큐**를 활용해 사용자 활동 로그를 전송하고, 별도의 로그 처리 서비스에서 **비동기로 수신 및 저장** 함으로써, 로그 저장 작업이 애플리케이션의 주요 프로세스에 영향을 주지 않고 원활히 수행되도록 설계
    - **저장 실패된 로그**는 별도로 관리하여 **consumer.log**파일로 저장하고 **데이터 손실을 방지**

- **텔레그램 알림**
    - 회원 가입 시 운영자가 누가 가입했는지 파악하고, 엑세스 로그 저장 중 장애 발생 시 신속한 문제 해결을 위해 **Telegram Bot API**를 통해 알림을 전송
    - 📖 **구현 예시**
              
        | 설명                     | 이미지                                                                                      |
        |--------------------------|---------------------------------------------------------------------------------------------|
        | **회원가입 알림**         | ![image](https://github.com/user-attachments/assets/400854a3-3d35-4d1f-9fb5-3095f949bc33) |
        | **엑세스 로그 장애 발생 알림** | ![image](https://github.com/user-attachments/assets/10f5bf5c-a682-4ed7-a5be-c6cdacf27c51)|

   
- **적금 만기 자동화**
    - **Spring Scheduler**를 사용해 매일 만기 도래 계좌를 확인하고, **만기 금액 입금** 및 **상태를 자동으로 업데이트**

- **Config 데이터 관리**
    - `yml` 파일의 **`access-log.enabled`** 설정 값에 따라 **`AccessLogFilter`의 활성화 여부를 제어**
    - **Redis 캐시**를 연동해 **자주 조회되는 Config 데이터**를 효율적으로 관리함으로써, 설정 변경 시 애플리케이션 **재배포 없이** Config 데이터와 캐시만 업데이트하면 되므로 **운영 부담 감소** 효과

- **이자 관리**
    - 적금 상품별로 **이자와 세금을 포함해 최종 금액을 계산**하는 기능 제공
    - 적금 **중도 해지** 시 예상 만기 이자를 계산하고, **중도 해지 패널티**를 적용한 이자 재계산 기능 구현

- **뷰 페이지**
    - **Thymeleaf**를 사용해 로그인 페이지 및 계좌 목록 페이지 등 간단한 뷰 페이지를 **서버 사이드 렌더링(SSR)** 방식으로 구현
    - **DataTables**를 활용해 계좌 목록 테이블의 **서버사이드 연동 및 페이지네이션** 구현
 
- **AWS 기반 배포**
    - **EC2** 인스턴스에 애플리케이션 배포 및 운영
    - **Caddy**를 웹 서버로 사용하여 HTTPS 인증서를 자동으로 관리하고, 로드밸런싱 없이 **Blue-Green 배포 환경** 구축
    - **Shell Script**를 작성해 build, jar 파일 전송, 서버 재시작 과정을 **자동화**하여 배포 효율성을 높임
    - **Docker**를 활용하여 RabbitMQ, Redis, Spring Boot 애플리케이션과 같은 서비스를 컨테이너로 분리 및 관리, 서비스의 **독립성**과 **확장성**을 확보



### 4. API 문서

| 번호 | Method | URL                                                    | Authorization |       기능        |
|:--:|:------:|--------------------------------------------------------|:-------------:|:---------------:|
| 1  | `POST` | `/api/bank/checking-accounts`                          |      ✔        |    입출금 계좌 생성    |
| 2  | `POST` | `/api/bank/saving-products/{savingProductId}/accounts` |      ✔        |    적금 계좌 생성     |
| 3  | `GET`  | `/api/bank/my-checking-accounts`                       |      ✔        |  나의 입출금 계좌 목록   |
| 4  | `GET`  | `/api/bank/my-saving-accounts`                         |      ✔        |   나의 적금 계좌 목록   |
| 5  | `GET`  | `/api/bank/saving-products`                            |      ✔         |    적금 상품 목록     |
| 6  | `GET`  | `/api/bank/saving-products-detail/{savingProductId}`   |      ✔         |    적금 상품 조회     |
| 7  | `PUT`  | `/api/bank/deposit`                                    |      ✔        |       입금        |
| 8  | `PUT`  | `/api/bank/withdrawal`                                 |      ✔        |       출금        |
| 9  | `PUT`  | `/api/bank/{fromAccountId}/transfer`                   |      ✔        |      계좌 이체      |
| 10 | `GET`  | `/api/bank/checking-transaction-history/{accountId}`   |      ✔        |    입출금 거래내역     |
| 11 | `GET`  | `/api/bank/saving-transaction-history/{accountId}`     |      ✔        |     적금 거래내역     |
| 12 | `POST` | `/api/bank/interest/calculate/{savingProductId}`       |       ✔        | 특정 적금 상품 이자 계산기 |
| 13 | `POST`  | `/api/bank/my-saving-accounts/{accountId}/terminate`   |       ✔        |    적금 중도 해지     |
| 14 | `GET`  | [`/health-check`](https://bankly.store/health-check)   |               |    시스템 진단     |


### 5. 서비스 및 포트 정보
- **Bank Service** (메인 서비스)
    - **URL**: [http://localhost:8080](http://localhost:8080)
    - **포트**: `8080`
    - **기능**: 비즈니스 로직을 수행하는 메인 서비스

- **AccessLog Service** (로그 관리)
    - **URL**: [http://localhost:8888](http://localhost:8888)
    - **포트**: `8888`
    - **기능**: 엑세스 로그를 수신하고 저장하는 전용 서비스
      

### 6. 로그 관리

- **서버 로그** (`server.log`)
    - **위치**: `./logs/server.log`
    - **설명**: 서버의 일반 작동 로그를 기록합니다.
    - **관리**: 매일 `server_YYYYMMDD.log.gz` 형식으로 압축 저장되며, 최대 90일간 보관

- **엑세스 로그** (`access.log`)
    - **위치**: `./logs/access.log`
    - **설명**: HTTP 요청에 대한 엑세스 로그를 기록하여 사용자 요청 정보를 저장합니다.
    - **관리**: 매일 `access_YYYYMMDD.log.gz` 형식으로 압축 저장, 최대 90일 보관

- **메시지 처리 로그** (`consumer.log`)
    - **위치**: `./logs/consumer.log`
    - **설명**: RabbitMQ 메시지 처리 중 엑세스 로그 저장에 실패한 메시지와 관련된 상태를 기록합니다.
    - **관리**: 매일 `consumer_YYYYMMDD.log.gz` 형식으로 압축 저장, 최대 90일간 보관


### 7. 주요 기능 화면

| 제목              | 이미지                                                                                      | 제목               | 이미지                                                                                      |
|-------------------|---------------------------------------------------------------------------------------------|--------------------|---------------------------------------------------------------------------------------------|
| **회원가입**    | ![회원가입](https://github.com/user-attachments/assets/de8a4c2c-2bf8-4973-8357-2e6d3530a37a) | **로그인**      | ![로그인](https://github.com/user-attachments/assets/b1d1a7fa-8e05-4051-929d-3069a4075b3a)
| **나의 계좌 목록**     | ![나의계좌목록](https://github.com/user-attachments/assets/b5ec6f25-f977-4cac-8a61-39386dea5a2e) | **거래내역 목록**      | ![거래내역목록](https://github.com/user-attachments/assets/5fcdb0bc-e35a-4b51-8675-2a692129ccc8) |
| **계좌이체**       | ![계좌이체_잔액부족](https://github.com/user-attachments/assets/7a5db6eb-616b-4044-b49e-ab66e5a55952) | **이체 완료**   | ![이체완료](https://github.com/user-attachments/assets/99d41829-3e0c-4ed9-ba29-db7243b3f9ae)   |
| **적금 상품 목록**     | ![적금상품목록](https://github.com/user-attachments/assets/9cd5efba-9841-4442-8c95-e32039f659ec) | **적금 상품 상세**     | ![적금상품상세](https://github.com/user-attachments/assets/bb129687-1eea-44ef-b0dd-838b8475f300) |
| **이자 계산기**        | ![이자계산기](https://github.com/user-attachments/assets/f3c24497-65c0-429a-8bd3-7944b778f338) | **적금상품 가입**      | ![적금상품한도_유효성](https://github.com/user-attachments/assets/dfdfee58-3929-45e5-aa23-0ec56b9b7941) |
| **엑세스 로그 시각화** | ![엑세스 로그](https://github.com/user-attachments/assets/06faf367-5915-4204-91a7-19ae4c3827a7) |





