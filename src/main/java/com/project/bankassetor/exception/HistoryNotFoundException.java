package com.project.bankassetor.exception;

import org.springframework.http.HttpStatus;

public class HistoryNotFoundException extends BankException{
    public HistoryNotFoundException() {
        super(ErrorCode.ACCOUNT_NOT_FOUND);
    }

    public HistoryNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public HistoryNotFoundException(HttpStatus httpStatus, ErrorCode errorCode) {
        super(errorCode);
    }
}
