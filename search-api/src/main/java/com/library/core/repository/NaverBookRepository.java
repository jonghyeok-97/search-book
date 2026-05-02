package com.library.core.repository;

import com.library.client.NaverClientCallException;
import com.library.client.NaverClient;
import com.library.client.request.NaverBookSortType;
import com.library.client.response.NaverBookResponse;
import com.library.core.domain.Book;
import com.library.core.support.Page;
import com.library.core.support.SortType;
import com.library.core.support.exception.CoreApiException;
import com.library.core.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NaverBookRepository {
    private final NaverClient naverClient;

    public Page<Book> search(String query, int page, int size, SortType sort) {
        try {
            NaverBookResponse naverBookResponse = naverClient.searchBook(query, page, size, NaverBookSortType.from(sort.name()));

            return Page.from(
                    naverBookResponse.total(),
                    naverBookResponse.items().stream()
                            .map(item -> Book.create(
                                    item.title(),
                                    item.author(),
                                    item.pubDate(),
                                    item.isbn(),
                                    item.description()
                            )).toList()
            );
        } catch (NaverClientCallException e) {
            throw new CoreApiException(ErrorType.EXTERNAL_API_ERROR, e);
        }
    }
}
