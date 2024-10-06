package com.project.bankassetor.exception;

import com.project.bankassetor.filter.AccessLogUtil;
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
        // ErrorCode 객체 가져오기
        ErrorCode errorCode = ex.getErrorCode();

        // Error ID 생성
        String errorId = AccessLogUtil.generateErrorId();

        // 에러 로그에 Error ID와 ErrorCode 정보 포함하여 기록
        log.error("Error ID: {}, Code: {}, Message: {}", errorId, errorCode.getCode(), errorCode.getDefaultMessage());

        return new RestError(errorCode.getCode(), errorCode.getDefaultMessage(), errorId);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestError handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorId = AccessLogUtil.generateErrorId();
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(x -> sb.append(x).append("\n"));

        // 에러 로그에 Error ID 포함
        log.error("Error ID: {}, Validation Error: {}", errorId, sb.toString());
        return new RestError(ErrorCode.BAD_REQUEST.getCode(), sb.toString().trim(), errorId);
    }

    @ExceptionHandler(value = Exception.class)
    public RestError unhandledException(Exception e, HttpServletRequest request) {
        String errorId = AccessLogUtil.generateErrorId();
        log.error("Error ID: {}, Unhandled Error: {}", errorId, e.getMessage(), e);
        return new RestError(ErrorCode.GENERAL_ERROR.getCode(), e.getMessage(), errorId);
    }

    @ExceptionHandler(value = BalanceNotEnoughException.class)
    public RestError handleBalanceNotEnoughException(BalanceNotEnoughException e) {
        String errorId = AccessLogUtil.generateErrorId();
        log.error("Error ID: {}, Balance Not Enough Error: {}", errorId, e.getMessage(), e);
        return new RestError(ErrorCode.BALANCE_NOT_ENOUGH.getCode(), e.getMessage(), errorId);
    }

    @ExceptionHandler(value = AccountNotFoundException.class)
    public RestError handleAccountNotFoundException(AccountNotFoundException e) {
        String errorId = AccessLogUtil.generateErrorId();
        log.error("Error ID: {}, Account Not Found Error: {}", errorId, e.getMessage(), e);
        return new RestError(ErrorCode.ACCOUNT_NOT_FOUND.getCode(), e.getMessage(), errorId);
    }

}