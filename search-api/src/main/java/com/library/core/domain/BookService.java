package com.library.core.domain;

import com.library.core.repository.NaverBookRepository;
import com.library.core.support.Page;
import com.library.core.support.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
    private final NaverBookRepository naverBookRepository;

    public Page<Book> search(String query, int page, int size, SortType sort) {
        return naverBookRepository.search(query, page, size, sort);
    }
}
