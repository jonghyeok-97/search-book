package com.library.core.domain;

import com.library.core.repository.NaverBookRepository;
import com.library.core.support.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    NaverBookRepository naverBookRepository;

    BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService(naverBookRepository);
    }

    @Test
    void search_인자가_그대로_repository에_전달된다() {
        given(naverBookRepository.search(anyString(), anyInt(), anyInt(), anyString()))
                .willReturn(Page.from(0, List.of()));

        bookService.search("HTTP", 1, 10, "date");

        then(naverBookRepository).should().search("HTTP", 1, 10, "date");
    }

    @Test
    void 책을_검색한다() {
        given(naverBookRepository.search(anyString(), anyInt(), anyInt(), anyString()))
                .willReturn(Page.from(
                        20,
                        List.of(Book.create("HTTP 완벽가이드", "조영호", "20220402", "A1002", "설명"),
                                Book.create("HTTP 동작 원리", "김영한", "20230402", "B1002", "설명"),
                                Book.create("HTTP 란?", "최배달", "20240402", "C1002", "설명"))
                ));

        Page<Book> search = bookService.search("HTTP", 1, 10, "date");

        assertThat(search.total()).isEqualTo(20);
        assertThat(search.contents()).containsExactly(
                Book.create("HTTP 완벽가이드", "조영호", "20220402", "A1002", "설명"),
                Book.create("HTTP 동작 원리", "김영한", "20230402", "B1002", "설명"),
                Book.create("HTTP 란?", "최배달", "20240402", "C1002", "설명")
        );
    }
}