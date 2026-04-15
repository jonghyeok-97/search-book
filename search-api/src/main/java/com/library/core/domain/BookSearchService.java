package com.library.core.domain;

import com.library.core.repository.NaverBookRepository;
import com.library.core.support.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookSearchService {
    private final NaverBookRepository naverBookRepository;

    public Page<Book> search(String query, int page, int size, String sort) {
        return naverBookRepository.search(query, page, size, sort);
    }
}
