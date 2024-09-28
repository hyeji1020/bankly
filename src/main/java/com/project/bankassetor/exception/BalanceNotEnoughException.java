package com.project.bankassetor.exception;

import org.springframework.http.HttpStatus;

public class BalanceNotEnoughException extends BankException{
    public BalanceNotEnoughException() {
        super(ErrorCode.BALANCE_NOT_ENOUGH);
    }

    public BalanceNotEnoughException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BalanceNotEnoughException(HttpStatus httpStatus, ErrorCode errorCode) {
        super(errorCode);
    }
}
