package com.library.core.domain;

import com.library.core.repository.DailyStat;
import com.library.core.repository.NaverBookRepository;
import com.library.core.support.Page;
import com.library.core.support.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookService {
    private final NaverBookRepository naverBookRepository;
    private final DailyStatAppender dailyStatAppender;

    public Page<Book> search(String query, int page, int size, SortType sort) {
        Page<Book> search = naverBookRepository.search(query, page, size, sort);

        dailyStatAppender.save(DailyStat.create(query, LocalDateTime.now()));

        return search;
    }
}
