package com.library.client;

public class KakaoClientCallException extends RuntimeException {

    public KakaoClientCallException(String message) {
        super(message);
    }

    public KakaoClientCallException(String message, Throwable cause) {
        super(message, cause);
    }
}
