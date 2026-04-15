package com.library.core.api;

import com.library.common.response.ApiResponse;
import com.library.core.api.response.BookResponse;
import com.library.core.domain.Book;
import com.library.core.domain.BookSearchService;
import com.library.core.support.Page;
import com.library.core.support.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookSearchService bookSearchService;

    @GetMapping("/books")
    public ApiResponse<PageResponse<BookResponse>> searchBooks(
            @RequestParam String query,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sort
    ) {
        Page<Book> paged = bookSearchService.search(query, page, size, sort);
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
