package com.library.core.support.response;

import com.library.core.support.exception.ErrorType;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private Result result;
    private T data;
    private Error error;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.result = Result.SUCCESS;
        response.data = data;
        return response;
    }

    public static <T> ApiResponse<T> error(ErrorType errorType) {
        ApiResponse<T> response = new ApiResponse<>();
        response.result = Result.ERROR;
        response.data = null;
        response.error = Error.from(errorType);
        return response;
    }

    public static <T> ApiResponse<T> error(ErrorType errorType, String description) {
        ApiResponse<T> response = new ApiResponse<>();
        response.result = Result.ERROR;
        response.data = null;
        response.error = Error.from(errorType, description);
        return response;
    }

    public enum Result {
        SUCCESS, ERROR
    }

    record Error(
            String type,
            String message,
            String description
    ) {
        public static Error from(ErrorType errorType) {
            return new Error(errorType.name(), errorType.getMessage(), errorType.getMessage());
        }

        public static Error from(ErrorType errorType, String description) {
            return new Error(errorType.name(), errorType.getMessage(), description);
        }
    }
}
