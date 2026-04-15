package com.library.core.support.exception;

import lombok.Getter;

@Getter
public class CoreApiException extends RuntimeException {
    private final ErrorType errorType;

    public CoreApiException(ErrorType errorType, Throwable cause) {
        super(cause);
        this.errorType = errorType;
    }

    public CoreApiException(ErrorType errorType) {
        this.errorType = errorType;
    }
}
