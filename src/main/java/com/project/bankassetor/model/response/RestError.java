package com.project.bankassetor.model.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RestError<T> implements Serializable {

    private String id;
    private String message;
    private String errorId;

    public RestError(String id, String message, String errorId) {
        this.id = id;
        this.message = message;
        this.errorId = errorId;
    }

}
