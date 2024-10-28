package com.project.bankassetor.primary.model.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RestError<T> implements Serializable {

    private int status;
    private String code;
    private String requestId;
    private String message;

    public RestError(int status, String code, String requestId, String message) {
        this.status = status;
        this.code = code;
        this.requestId = requestId;
        this.message = message;
    }

}
