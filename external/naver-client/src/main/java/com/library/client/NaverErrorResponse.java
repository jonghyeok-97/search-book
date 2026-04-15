package com.library.client;

public record NaverErrorResponse(
        String errorCode,
        String errorMessage
) {
}
