package com.library.core.api;

import com.library.core.api.request.BookSearchRequest;
import com.library.core.api.response.BookResponse;
import com.library.core.api.response.QueryStatResponse;
import com.library.core.domain.Book;
import com.library.core.domain.BookService;
import com.library.core.support.Page;
import com.library.core.support.response.ApiResponse;
import com.library.core.support.response.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Validated
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

    @GetMapping("/v1/books/stats/daily")
    public ApiResponse<Long> findDailyStats(@RequestParam String query, @RequestParam LocalDate date) {
        return ApiResponse.success(bookService.findQueryStats(query, date));
    }

    @GetMapping("/v1/books/stats/query")
    public ApiResponse<List<QueryStatResponse>> findTopQueries(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end,
            @RequestParam @Min(1) @Max(5) int size
    ) {
        return ApiResponse.success(bookService.findTopQueryStats(start, end, size).stream()
                .map(QueryStatResponse::from)
                .toList());
    }

}
