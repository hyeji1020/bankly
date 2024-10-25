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

    GENERAL_ERROR("BANK_ERR_999", "서버 내부 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND("BANK_ERR_005", "해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    ACCESS_LOG_ERROR("ACCESS_LOG_ERR_001", "엑세스 로그 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ACCESS_LOG_BATCH_SAVE_ERROR("ACCESS_LOG_ERR_002", "엑세스 로그 배치 저장 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    CONFIG_NOT_FOUND_ERROR("CONFIG_NOT_FOUND", "해당 설정이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);

    private final String code;            // 에러 고유 코드
    private final String defaultMessage;  // 기본 에러 메시지
    private final HttpStatus status;      // HTTP 상태 코드
}
