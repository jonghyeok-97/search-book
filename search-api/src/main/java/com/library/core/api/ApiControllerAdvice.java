package com.library.core.api;

import com.library.common.exception.CoreApiException;
import com.library.common.exception.ErrorType;
import com.library.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(CoreApiException.class)
    public ResponseEntity<?> handleCoreApiException(CoreApiException e) {
        ErrorType errorType = e.getErrorType();
        LogLevel logLevel = errorType.getLogLevel();
        switch (logLevel) {
            case ERROR:
                log.error("CoreApiException: {}", e.getMessage(), e);
            case WARN:
                log.warn("CoreApiException: {}", e.getMessage(), e);
            default:
                log.info("CoreApiException: {}", e.getMessage(), e);
        }
        return ResponseEntity.status(errorType.getStatus().value())
                .body(ApiResponse.fail(errorType.getMessage()));
    }
}
