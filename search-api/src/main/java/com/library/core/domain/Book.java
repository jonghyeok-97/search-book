package com.library.core.domain;

public record Book(
        String title,
        String author,
        String publishDate,
        String isbn,
        String description
) {
    public static Book create(String title, String author, String publishDate, String isbn, String description) {
        return new Book(title, author, publishDate, isbn, description);
    }
}
