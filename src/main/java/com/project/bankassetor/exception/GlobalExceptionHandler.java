package com.project.bankassetor.exception;

import com.project.bankassetor.model.response.RestError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BankException.class)
    public RestError handleBankException(BankException ex, HttpServletRequest request) {
        // ErrorCode 객체 가져오기
        ErrorCode errorCode = ex.getErrorCode();

        String requestId = (String) request.getAttribute("requestId");

        // 에러 로그에 RequestId와 ErrorCode 정보 포함하여 기록
        log.error("RequestID: {}, Code: {}, Message: {}", requestId, errorCode.getCode(), errorCode.getDefaultMessage());

        return new RestError(errorCode.getStatus().value(), errorCode.getCode(), requestId, errorCode.getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestError handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String requestId = (String) request.getAttribute("requestId");
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(x -> sb.append(x).append("\n"));

        // 에러 로그에 Error ID 포함
        log.error("RequestID: {}, Validation Error: {}", requestId, sb.toString());
        return new RestError(HttpStatus.BAD_REQUEST.value(), ErrorCode.BAD_REQUEST.getCode(), requestId, sb.toString().trim());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public RestError unhandledException(Exception e, HttpServletRequest request) {
        String requestId = (String) request.getAttribute("requestId");
        log.error("RequestID: {}, Unhandled Error: {}", requestId, e.getMessage(), e);
        return new RestError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.GENERAL_ERROR.getCode(), requestId, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BalanceNotEnoughException.class)
    public RestError handleBalanceNotEnoughException(BalanceNotEnoughException e, HttpServletRequest request) {
        String requestId = (String) request.getAttribute("requestId");
        log.error("RequestID: {}, Balance Not Enough Error: {}", requestId, e.getMessage(), e);
        return new RestError(HttpStatus.BAD_REQUEST.value(), ErrorCode.BALANCE_NOT_ENOUGH.getCode(), requestId, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = AccountNotFoundException.class)
    public RestError handleAccountNotFoundException(AccountNotFoundException e, HttpServletRequest request) {
        String requestId = (String) request.getAttribute("requestId");
        log.error("RequestID: {}, Account Not Found Error: {}", requestId, e.getMessage(), e);
        return new RestError(HttpStatus.BAD_REQUEST.value(), ErrorCode.ACCOUNT_NOT_FOUND.getCode(), requestId, e.getMessage());
    }

}