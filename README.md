<div align="center">
    
# 💰Bankly 
은행 서비스를 위한 플랫폼

### 📌 서비스 개요
숫자와 금액처럼 민감한 데이터를 다루는 은행 서비스를 구현하며,<br>
**정확성**과 **신뢰성**을 갖춘 플랫폼 개발 역량을 키우고자 이 프로젝트를 진행했습니다.<br>
**실시간 계좌 거래**와 **적금 만기 시 이자 지급** 시스템을 통해 실제 금융 서비스와 유사한 환경을 제공합니다.

### 🛠️ 기술 스택
![Java](https://img.shields.io/badge/Java-21-blue?logo=openjdk&logoColor=white)       
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen?logo=springboot) 
![JPA](https://img.shields.io/badge/JPA-ORM-orange)                                  
![Spring Security](https://img.shields.io/badge/Spring%20Security-auth-green?logo=springsecurity) 
![MySQL](https://img.shields.io/badge/MySQL-8.0.32-blue?logo=mysql&logoColor=white)  
![Redis](https://img.shields.io/badge/Redis-Cache-red?logo=redis&logoColor=white)    
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Message%20Broker-orange?logo=rabbitmq) 


</div>


# 📋 목차
1. [서비스 아키텍처 및 시퀀스 다이어그램](#1-서비스-아키텍처-및-시퀀스-다이어그램)  
2. [ERD 및 디렉토리 구조](#2-erd-및-디렉토리-구조)  
3. [주요 기능](#3-주요-기능)  
4. [API 문서](#4-api-문서)  
5. [서비스 및 포트 정보](#5-서비스-및-포트-정보)  
6. [로그 관리](#6-로그-관리)  
7. [빌드 및 실행 방법](#7-빌드-및-실행-방법)  

### 1. 서비스 아키텍처 및 시퀀스 다이어그램

<details>
<summary>서비스 아키텍처</summary>
    
</details>
<details>
<summary>시퀀스 다이어그램</summary>
    
![bankly-sequenceDiagram](https://github.com/user-attachments/assets/6bb25475-a23d-4592-9e6d-77ee1d8d7875)

</details>

### 2. ERD 및 디렉터리 구조

<details>
<summary>ERD</summary>
    
![image](https://github.com/user-attachments/assets/b1e316fa-1734-4467-8f03-7875cf1c41d4)
</details>
<details>
    
<summary>디렉터리 구조</summary>

</details>

### 3. 주요 기능
- **로그인 및 회원가입**
    - **Spring Security**를 활용해 회원 가입 시 비밀번호 암호화 및 권한 부여 로직 구현, 로그인 기능 설계
    - **Session 관리**를 통해 사용자 인증 상태 유지

- **계좌 관리**
    - 당좌 계좌 및 적금 계좌 **생성 및 조회** 기능 구현
    - Spring MVC 패턴을 활용해 계좌 간 **입금, 출금, 계좌이체** 기능 개발
    - 계좌별 **거래 내역 조회** 기능 제공

- **엑세스 로그 관리**
    - **RabbitMQ 기반 메시지 큐**를 활용해 사용자 활동 로그를 전송하고, 별도의 로그 처리 서비스에서 **비동기로 수신 및 저장**
    - **저장 실패된 로그**는 별도로 관리하여 **consumer.log**파일로 저장

- **텔레그램 알림**
    - 새로운 회원 가입 및 엑세스 로그 저장 중 장애 또는 예외 발생 시 **Telegram Bot API**를 통해 알림 전송

- **적금 만기 자동화**
    - **Spring Batch**를 사용해 매일 만기 도래 계좌를 확인하고, **만기 금액 입금** 및 계좌 상태를 자동으로 업데이트

- **Config 데이터 관리**
    - `yml` 파일의 **`access-log.enabled`** 설정 값에 따라 `AccessLogFilter`의 활성화 여부를 제어
    - **Redis 캐시**를 연동해 자주 조회되는 Config 데이터를 효율적으로 관리

- **이자 관리**
    - 적금 상품별로 **이자와 세금을 포함해 최종 금액을 계산**하는 기능 제공
    - 적금 **중도 해지** 시 예상 만기 이자를 계산하고, **중도 해지 패널티**를 적용한 이자 재계산 기능 구현

- **뷰 페이지**
    - **Thymeleaf**를 사용해 로그인 페이지 및 계좌 목록 페이지 등 간단한 뷰 페이지를 **서버 사이드 렌더링(SSR)** 방식으로 구현
    - **DataTables**를 활용해 계좌 목록 테이블의 **서버사이드 연동 및 페이지네이션** 구현


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


### 5. 서비스 및 포트 정보
- **Bank Service** (메인 서비스)
    - **URL**: [http://localhost:8080](http://localhost:8080)
    - **포트**: `8080`
    - **기능**: 비즈니스 로직을 수행하는 메인 서비스

- **AccessLog Service** (로그 관리)
    - **URL**: [http://localhost:8888](http://localhost:8888)
    - **포트**: `8888`
    - **기능**: 엑세스 로그를 수신하고 저장하는 전용 서비스
    - 

### 6. 로그 관리

- **서버 로그** (`server.log`)
    - **위치**: `./logs/server.log`
    - **설명**: 서버의 일반 작동 로그를 기록합니다.
    - **관리**: 매일 `server_YYYYMMDD.log.gz` 형식으로 압축 저장되며, 최근 90일간 보관

- **엑세스 로그** (`access.log`)
    - **위치**: `./logs/access.log`
    - **설명**: HTTP 요청에 대한 엑세스 로그를 기록하여 사용자 요청 정보를 저장합니다.
    - **관리**: 매일 `access_YYYYMMDD.log.gz` 형식으로 압축 저장, 최대 90일 보관

- **메시지 처리 로그** (`consumer.log`)
    - **위치**: `./logs/consumer.log`
    - **설명**: RabbitMQ 메시지 처리 중 엑세스 로그 저장에 실패한 메시지와 관련된 상태를 기록합니다.
    - **관리**: 매일 `consumer_YYYYMMDD.log.gz` 형식으로 압축 저장, 최근 90일간 보관


### 7. 빌드 및 실행 방법
추후에 추가 예정입니다.
