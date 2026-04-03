package com.library.common.exception;

import lombok.Getter;

@Getter
public class CoreApiException extends RuntimeException {
    private final ErrorType errorType;

    public CoreApiException(String logMessage, ErrorType errorType, Throwable cause) {
        super(logMessage, cause);
        this.errorType = errorType;
    }

    public CoreApiException(String logMessage, ErrorType errorType) {
        super(logMessage);
        this.errorType = errorType;
    }
}
