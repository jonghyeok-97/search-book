package com.library.client.response;

public record NaverBookItem(
        String title,
        String link,
        String image,
        String author,
        String discount,
        String publisher,
        String pubdate,
        String isbn,
        String description
) {
}
