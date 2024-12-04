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
1. [서비스 아키텍처 및 UML](#1-서비스-아키텍처-및-UML)  
2. [ERD 및 디렉토리 구조](#2-erd-및-디렉토리-구조)  
3. [주요 기능](#3-주요-기능)  
4. [API 문서](#4-api-문서)  
5. [서비스 및 포트 정보](#5-서비스-및-포트-정보)  
6. [로그 관리](#6-로그-관리)  
7. [빌드 및 실행 방법](#7-빌드-및-실행-방법)  

### 1. 서비스 아키텍처 및 UML

<details>
<summary>서비스 아키텍처</summary>
    
</details>
<details>
<summary>UML</summary>


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

- **계좌 관리**  
  당좌 계좌 및 적금 계좌 생성 및 조회 기능 제공.

- **입출금 및 이체**  
  Spring MVC 패턴을 활용하여 계좌 간 입금, 출금, 계좌이체 기능 구현.

- **거래 내역 조회**  
  계좌별 거래 내역 조회 API 제공.

- **엑세스 로그 관리**  
  RabbitMQ 기반 메시지 큐를 활용하여 사용자 활동 로그를 비동기로 수신 및 저장.

- **텔레그램 알림**  
  새로운 회원 가입 시 및 엑세스 로그 저장 중 치명적인 오류 발생 시 Telegram Bot API를 통해 알림 전송.

- **적금 만기 자동화**  
  Spring Batch를 사용해 매일 만기 도래 계좌를 검증하고, 만기 금액 입금 및 계좌 상태를 자동으로 업데이트.

- **적금 상품 관리**  
  적금 상품 조회 및 적금 계좌 생성 API 제공.

- **이자 계산기**  
  적금 상품별로 이자와 세금을 포함한 최종 금액을 계산하는 기능 구현.

- **패널티 정책**  
  적금 중도 해지 시 예상 만기 이자 확인 및 패널티를 적용하여 이자를 재계산.

- **뷰 페이지**  
  Thymeleaf를 사용한 서버 사이드 렌더링(SSR) 방식으로 간단한 뷰 페이지 구현.


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
    - **설명**: 메시지 큐(MQ) 작업을 기록하여 메시지 처리 상태를 모니터링합니다.
    - **관리**: 매일 `consumer_YYYYMMDD.log.gz` 형식으로 압축 저장, 최근 90일간 보관


### 7. 빌드 및 실행 방법
추후에 추가 예정입니다.
