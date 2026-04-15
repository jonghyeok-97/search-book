package com.library.client.request;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum NaverBookSortType {
    SIM("sim"),
    DATE("date")
    ;
    private final String value;

    public static NaverBookSortType from(String target) {
        return Arrays.stream(values())
                .filter(type -> type.value.equalsIgnoreCase(target))
                .findFirst()
                .orElse(SIM);
    }
}
