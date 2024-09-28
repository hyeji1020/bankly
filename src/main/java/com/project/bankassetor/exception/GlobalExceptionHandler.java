package com.project.bankassetor.exception;

import com.project.bankassetor.model.response.RestError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BankException.class)
    public RestError handleBankException(BankException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return new RestError(errorCode.getCode(), errorCode.getDefaultMessage());  // 에러 발생 시 RestError 반환
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestError handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(x -> sb.append(x).append("\n"));
        return new RestError(ErrorCode.BAD_REQUEST.getCode(), sb.toString().trim());
    }

    @ExceptionHandler(value = Exception.class)
    public RestError unhandledException(Exception e, HttpServletRequest request) {
        log.error("error occur {}", e.getMessage(), e);
        return new RestError(ErrorCode.GENERAL_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = BalanceNotEnoughException.class)
    public RestError handleBalanceNotEnoughException(BalanceNotEnoughException e) {
        return new RestError(ErrorCode.BALANCE_NOT_ENOUGH.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = AccountNotFoundException.class)
    public RestError handleAccountNotFoundException(AccountNotFoundException e) {
        return new RestError(ErrorCode.ACCOUNT_NOT_FOUND.getCode(), e.getMessage());
    }

}