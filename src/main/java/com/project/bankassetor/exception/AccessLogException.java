package com.project.bankassetor.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessLogException extends RuntimeException{

    private final ErrorCode errorCode;

    public AccessLogException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

}
