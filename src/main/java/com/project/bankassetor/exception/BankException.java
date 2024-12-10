package com.project.bankassetor.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankException extends RuntimeException{

    private final ErrorCode errorCode;

    public BankException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

}
