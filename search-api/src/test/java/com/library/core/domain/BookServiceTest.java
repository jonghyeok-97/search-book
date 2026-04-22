package com.library.core.domain;

import com.library.core.IntegrationSupport;
import com.library.core.repository.DailyStat;
import com.library.core.repository.NaverBookRepository;
import com.library.core.support.Page;
import com.library.core.support.SortType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
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

    @Autowired
    BookService bookService;

    @Test
    void search_인자가_그대로_repository에_전달된다() {
        given(naverBookRepository.search(anyString(), anyInt(), anyInt(), any()))
                .willReturn(Page.from(0, List.of()));
        String givenQuery = "HTTP";

        bookService.search(givenQuery, 1, 10, SortType.DATE);

        then(naverBookRepository).should().search(givenQuery, 1, 10, SortType.DATE);
        verify(dailyStatAppender, times(1)).save(any(DailyStat.class));
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
}