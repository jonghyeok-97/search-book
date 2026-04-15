package com.library.core.repository;

import com.library.client.NaverClient;
import com.library.client.request.NaverBookSortType;
import com.library.client.response.NaverBookResponse;
import com.library.core.domain.Book;
import com.library.core.support.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NaverBookRepository {
    private final NaverClient naverClient;

    public Page<Book> search(String query, int page, int size, String sort) {
        NaverBookResponse naverBookResponse = naverClient.search(query, page, size, NaverBookSortType.from(sort));

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
    }
}
