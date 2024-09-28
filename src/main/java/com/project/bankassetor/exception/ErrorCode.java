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
    GENERAL_ERROR("BANK_ERR_999", "서버 내부 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;            // 에러 고유 코드
    private final String defaultMessage;  // 기본 에러 메시지
    private final HttpStatus status;      // HTTP 상태 코드
}
