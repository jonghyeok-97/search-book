package com.library.core.repository;

import com.library.client.NaverClient;
import com.library.client.NaverClientCallException;
import com.library.client.request.NaverBookSortType;
import com.library.client.response.NaverBookResponse;
import com.library.core.domain.Book;
import com.library.core.support.Page;
import com.library.core.support.SortType;
import com.library.core.support.exception.CoreApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class NaverBookRepositoryTest {

    @Mock
    NaverClient naverClient;

    NaverBookRepository naverBookRepository;

    @BeforeEach
    void setUp() {
        naverBookRepository = new NaverBookRepository(naverClient);
    }

    @Test
    void search() {
        given(naverClient.search(anyString(), anyInt(), anyInt(), any(NaverBookSortType.class)))
                .willReturn(new NaverBookResponse(
                        "", 10L, 1L, 10L, List.of()
                ));

        Page<Book> paged = naverBookRepository.search(
                "HTTP 완벽 가이드",
                1,
                10,
                SortType.DATE
        );

        assertThat(paged.total()).isEqualTo(10);
    }

    @Test
    void NaverCallException예외는_CoreApiException으로_래핑된다() {
        given(naverClient.search(anyString(), anyInt(), anyInt(), any(NaverBookSortType.class)))
                .willThrow(new NaverClientCallException("외부 API 호출 중 에러 발생"));

        assertThatThrownBy(() -> naverBookRepository.search("HTTP", 1, 10, SortType.DATE))
                .isInstanceOf(CoreApiException.class);
    }
}