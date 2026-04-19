package com.library.core.support.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, LogLevel.ERROR, "알 수 없는 에러입니다"),
    EXTERNAL_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, LogLevel.ERROR, "외부 API 호출 에러입니다"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, LogLevel.WARN, "유효하지 않은 파라미터입니다.")

    ;
    private final HttpStatus status;
    private final LogLevel logLevel;
    private final String message;
}
