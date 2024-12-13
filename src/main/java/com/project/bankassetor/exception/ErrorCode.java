package com.project.bankassetor.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ACCOUNT_NOT_FOUND("BANK_ERR_001", "해당 계좌를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    BALANCE_NOT_ENOUGH("BANK_ERR_002", "계좌 잔액이 부족합니다.", HttpStatus.BAD_REQUEST),
    BAD_REQUEST("BANK_ERR_003", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    SAVING_ACCOUNT_NOT_FOUND("BANK_ERR_004", "해당 적금 상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND ),
    DEPOSIT_LIMIT_EXCEEDED("BANK_ERR_005", "입금 한도를 초과했습니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("BANK_ERR_006", "해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ACCOUNT_ALREADY_TERMINATE("BANK_ERR_007", "해당 계좌가 이미 해지되었습니다.", HttpStatus.BAD_REQUEST),
    INVALID_ACCOUNT_OWNER("BANK_ERR_008", "해당 계좌의 소유자가 아닙니다.", HttpStatus.BAD_REQUEST),
    OVER_SAVING_LIMIT("BANK_ERR_009", "적금 한도보다 큽니다.", HttpStatus.BAD_REQUEST),
    INVALID_TRANSFER_ACCOUNT("BANK_ERR_010", "유효하지 않은 이체 요청 입니다.(동일 계좌 포함)", HttpStatus.BAD_REQUEST),

    GENERAL_ERROR("BANK_ERR_999", "서버 내부 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    ACCESS_LOG_ERROR("ACCESS_LOG_ERR_001", "엑세스 로그 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ACCESS_LOG_BATCH_SAVE_ERROR("ACCESS_LOG_ERR_002", "엑세스 로그 배치 저장 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ACCESS_LOG_IP_LOCATION_ERROR("ACCESS_LOG_ERR_003", "엑세스 로그 IP 기반 위치를 가져올 수 없습니다..", HttpStatus.INTERNAL_SERVER_ERROR),

    CONFIG_NOT_FOUND_ERROR("CONFIG_NOT_FOUND", "해당 설정이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),

    USER_EMAIL_DUPLICATE("AUTH_ERR_001", "이미 가입되어 있는 이메일입니다.", HttpStatus.BAD_REQUEST);

    private final String code;            // 에러 고유 코드
    private final String defaultMessage;  // 기본 에러 메시지
    private final HttpStatus status;      // HTTP 상태 코드
}
