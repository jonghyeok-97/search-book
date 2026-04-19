package com.library.core.api;

import com.library.core.support.exception.CoreApiException;
import com.library.core.support.exception.ErrorType;
import com.library.core.support.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(CoreApiException.class)
    public ResponseEntity<?> handleCoreApiException(CoreApiException e) {
        ErrorType errorType = e.getErrorType();
        LogLevel logLevel = errorType.getLogLevel();
        switch (logLevel) {
            case ERROR -> log.error("CoreApiException: {}", e.getMessage(), e);
            case WARN -> log.warn("CoreApiException: {}", e.getMessage(), e);
            default -> log.info("CoreApiException: {}", e.getMessage(), e);
        }
        return ResponseEntity.status(errorType.getStatus().value())
                .body(ApiResponse.error(errorType));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handleBindException(BindException e) {
        log.warn("BindException: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ErrorType.INVALID_PARAMETER, extractMessage(e)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("UNKNOWN Exception: {} ", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ErrorType.UNKNOWN_ERROR));
    }

    private String extractMessage(BindException e) {
        if (e.getFieldError() != null && e.getFieldError().getDefaultMessage() != null) {
            return e.getFieldError().getDefaultMessage();
        }

        return e.getFieldErrors().stream()
                .map(FieldError::getField)
                .collect(Collectors.joining(", "));
    }


}
