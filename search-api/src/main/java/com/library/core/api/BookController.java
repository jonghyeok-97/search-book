package com.library.core.api;

import com.library.core.api.request.BookSearchRequest;
import com.library.core.api.response.BookResponse;
import com.library.core.domain.Book;
import com.library.core.domain.BookService;
import com.library.core.support.Page;
import com.library.core.support.response.ApiResponse;
import com.library.core.support.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/v1/books")
    public ApiResponse<PageResponse<BookResponse>> searchBooks(@Valid @ModelAttribute BookSearchRequest request) {
        Page<Book> paged = bookService.search(request.query(), request.page(), request.size(), request.sort());
        return ApiResponse.success(
                PageResponse.from(
                        paged.total(),
                        paged.contents()
                                .stream()
                                .map(BookResponse::from)
                                .toList()
                )
        );
    }

}
