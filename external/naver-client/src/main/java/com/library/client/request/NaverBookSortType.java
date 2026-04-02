package com.library.client.request;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NaverBookSortType {
    SIM("sim"),
    DATE("date")
    ;
    private final String value;
}
