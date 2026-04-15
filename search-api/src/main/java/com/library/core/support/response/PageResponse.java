package com.library.core.support.response;

import java.util.List;

public record PageResponse<T>(
        long total,
        List<T> content
) {
    public static <T> PageResponse<T> from(long total, List<T> content) {
        return new PageResponse<>(total, content);
    }
}
