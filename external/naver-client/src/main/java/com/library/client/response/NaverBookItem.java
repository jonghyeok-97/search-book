package com.library.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NaverBookItem(
        String title,
        String link,
        String image,
        String author,
        String discount,
        String publisher,
        @JsonProperty("pubdate")
        String pubDate,
        String isbn,
        String description
) {
}
