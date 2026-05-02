package com.library.client.response;

import java.time.OffsetDateTime;
import java.util.List;

public record Document(
        String title,
        String contents,
        String url,
        String isbn,
        OffsetDateTime datetime,
        List<String> authors,
        String publisher,
        List<String> translators,
        Integer price,
        Integer salePrice,
        String thumbnail,
        String status
) {
}
