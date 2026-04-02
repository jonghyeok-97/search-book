package com.library.client.response;

import java.util.List;

public record NaverBookResponse(
        String lastBuildDate,
        Long total,
        Long start,
        Long display,
        List<NaverBookItem> items
) {
}
