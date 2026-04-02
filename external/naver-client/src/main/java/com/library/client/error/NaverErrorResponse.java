package com.library.client.error;

public record NaverErrorResponse(
        String errorCode,
        String errorMessage
) {
}
