package com.library.client;

public class NaverClientCallException extends RuntimeException {

    public NaverClientCallException(String message) {
        super(message);
    }

    public NaverClientCallException(String message, Throwable cause) {
        super(message, cause);
    }
}
