package com.library.core.api.response;

import com.library.core.domain.Book;

public record BookResponse(
        String author,
        String title,
        String description
) {
    public static BookResponse from(Book book) {
        return new BookResponse(
                book.author(),
                book.title(),
                book.description()
        );
    }
}
