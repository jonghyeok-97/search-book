package com.library.client.response;

public record KakaoErrorResponse(
        String errorType,
        String message
) {
}
