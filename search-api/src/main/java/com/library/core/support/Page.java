package com.library.core.support;

import java.util.List;

public record Page<T>(
        long total,
        List<T> contents
) {
    public static <T> Page<T> from(long total, List<T> contents) {
        return new Page<>(total, contents);
    }
}
