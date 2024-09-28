package com.project.bankassetor.model.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RestError<T> implements Serializable {

    private String id;
    private String message;

    public RestError(String id, String message) {
        this.id = id;
        this.message = message;
    }
}
