package com.library.core.domain;

import com.library.core.IntegrationSupport;
import com.library.core.repository.DailyStat;
import com.library.core.repository.NaverBookRepository;
import com.library.core.support.Page;
import com.library.core.support.SortType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

class BookServiceTest extends IntegrationSupport {

    @MockitoBean
    NaverBookRepository naverBookRepository;

    @MockitoBean
    DailyStatAppender dailyStatAppender;

    @MockitoBean
    DailyStatFinder dailyStatFinder;

    @Autowired
    BookService bookService;

    @Test
    void search_인자가_그대로_repository에_전달된다() {
        given(naverBookRepository.search(anyString(), anyInt(), anyInt(), any()))
                .willReturn(Page.from(0, List.of()));
        String givenQuery = "HTTP";

        bookService.search(givenQuery, 1, 10, SortType.DATE);

        then(naverBookRepository).should().search(givenQuery, 1, 10, SortType.DATE);
        then(dailyStatAppender).should().save(any(DailyStat.class));
    }

    @Test
    void 책을_검색한다() {
        given(naverBookRepository.search(anyString(), anyInt(), anyInt(), any()))
                .willReturn(Page.from(
                        20,
                        List.of(Book.create("HTTP 완벽가이드", "조영호", "20220402", "A1002", "설명"),
                                Book.create("HTTP 동작 원리", "김영한", "20230402", "B1002", "설명"),
                                Book.create("HTTP 란?", "최배달", "20240402", "C1002", "설명"))
                ));
        String givenQuery = "HTTP";

        Page<Book> search = bookService.search(givenQuery, 1, 10, SortType.DATE);

        verify(dailyStatAppender, times(1)).save(any(DailyStat.class));
        assertThat(search.total()).isEqualTo(20);
        assertThat(search.contents()).containsExactly(
                Book.create("HTTP 완벽가이드", "조영호", "20220402", "A1002", "설명"),
                Book.create("HTTP 동작 원리", "김영한", "20230402", "B1002", "설명"),
                Book.create("HTTP 란?", "최배달", "20240402", "C1002", "설명")
        );
    }

    @Test
    void 일일통계_구할_때_인자를_그대로_넘긴다() {
        String givenQuery = "HTTP";
        LocalDate givenDate = LocalDate.now();
        bookService.findQueryStats(givenQuery, givenDate);

        then(dailyStatFinder).should().readDailyCount(givenQuery, givenDate);
    }

    @Test
    void 상위쿼리_검색하면_인자를_그대로_넘긴다() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(1);
        int size = 3;

        bookService.findTopQueryStats(start, end, size);

        verify(dailyStatFinder).findTopQuery(start, end, size);
    }
}