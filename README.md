# 💰Bankly
은행 서비스를 위한 플랫폼

# 목차

1. [개요](#1-개요)
2. [개발 환경](#2-개발-환경)
3. [서비스 및 포트 정보](#3-서비스-및-포트-정보)
4. [ERD](#4-erd)
5. [디렉토리 구조](#5-디렉토리-구조)
6. [주요 기능](#6-주요-기능)
7. [API 문서](#7-api-문서)
8. [로그 관리](#8-로그-관리)
9. [빌드 및 실행 방법](#9-빌드-및-실행-방법)
10. [기타 정보](#10-기타-정보)

---

## 1. 개요
숫자와 돈처럼 민감한 데이터를 다루는 은행 서비스 구현을 통해 실수 없이 신뢰성 있는 플랫폼 개발 역량을 키우고자 은행 서비스를 구현 했습니다.


## 2. 개발 환경
- **Java**: 21
- **Spring Boot**: 3.3.4
- **JPA**: 데이터베이스와의 ORM(Object Relational Mapping)
- **Spring Security**: 인증 및 권한 관리
- **MySQL Connector**: 8.0.32
- **H2 Database**: 테스트용 내장 데이터베이스
- **Redis**: 캐싱 및 세션 관리용 메모리 데이터베이스
- **RabbitMQ**: 메시지 브로커


## 3. 서비스 및 포트 정보
- **Bank Service** (메인 서비스)
    - **URL**: [http://localhost:8080](http://localhost:8080)
    - **포트**: `8080`
    - **기능**: 비즈니스 로직을 수행하는 메인 서비스

- **AccessLog Service** (로그 관리)
    - **URL**: [http://localhost:8888](http://localhost:8888)
    - **포트**: `8888`
    - **기능**: 엑세스 로그를 수신하고 저장하는 전용 서비스


## 4. ERD
https://www.erdcloud.com/d/NZ8yudZcqc7kb6fsR

## 5. 디렉터리 구조
추후에 추가 예정입니다.

## 6. 주요 기능
1. **계좌 관리**: 당좌 계좌 및 적금 계좌 생성 및 조회
2. **입출금 및 이체**: 계좌에 대한 입금, 출금, 계좌 간 이체 기능
3. **거래 내역 조회**: 계좌별 거래 내역 조회
4. **엑세스 로그 관리**: 엑세스 로그를 별도 서비스에서 수신 및 저장
5. **텔레그램 알림**: 새로운 회원 가입 및 엑세스 로그 저장 시 치명적 오류 발생 시 알림 전송
6. **적금 만기 자동화**: 스프링 배치를 통해 만기 도래 계좌 조회 및 만기 금액 입금을 자동 처리
7. **적금 상품 관리**: 적금 상품 조회 및 적금 계좌 생성
8. **이자 계산기**: 적금 상품별 이자 계산 기능
9. **패널티 정책**: 적금 중도 해지 시 패널티 적용하여 이자 계산


## 7. API 문서

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

## 8. 로그 관리

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


## 9. 빌드 및 실행 방법
추후에 추가 예정입니다.