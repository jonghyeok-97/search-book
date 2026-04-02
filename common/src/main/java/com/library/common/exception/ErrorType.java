package com.library.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    EXTERNAL_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, LogLevel.ERROR, "외부 API 호출 에러입니다"),

    ;

    private final HttpStatus status;
    private final LogLevel logLevel;
    private final String message;
}
