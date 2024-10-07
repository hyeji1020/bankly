package com.project.bankassetor.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BankException{

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }

    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserNotFoundException(HttpStatus httpStatus, ErrorCode errorCode) {
        super(errorCode);
    }
}
