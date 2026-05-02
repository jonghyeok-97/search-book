package com.library.client.response;

public record NaverErrorResponse(
        String errorCode,
        String errorMessage
) {
}
