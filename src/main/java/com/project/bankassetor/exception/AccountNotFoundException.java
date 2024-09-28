package com.project.bankassetor.exception;

import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends BankException{
    public AccountNotFoundException() {
        super(ErrorCode.ACCOUNT_NOT_FOUND);
    }

    public AccountNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AccountNotFoundException(HttpStatus httpStatus, ErrorCode errorCode) {
        super(errorCode);
    }
}
