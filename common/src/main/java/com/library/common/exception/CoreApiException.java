package com.library.common.exception;

import lombok.Getter;

@Getter
public class CoreApiException extends RuntimeException {
    private final String logMessage;
    private final ErrorType errorType;

    public CoreApiException(String logMessage, ErrorType errorType) {
        super(logMessage);
        this.logMessage = logMessage;
        this.errorType = errorType;
    }
}
