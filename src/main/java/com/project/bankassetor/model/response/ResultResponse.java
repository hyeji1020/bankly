package com.project.bankassetor.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@Setter
public class ResultResponse<T> implements Serializable {
    private int status;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public ResultResponse() {
        this.status = HttpStatus.OK.value();
        this.message = "success";
    }

    public ResultResponse(T result) {
        this.status = HttpStatus.OK.value();
        this.message = "success";
        this.result = result;
    }

    public ResultResponse(HttpStatus httpStatus) {
        this.status = httpStatus.value();
    }

    public ResultResponse(String message) {
        this.status = HttpStatus.OK.value();
        this.message = message;
    }

    public ResultResponse(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
    }

    public T getResult() {
        return result;
    }
}