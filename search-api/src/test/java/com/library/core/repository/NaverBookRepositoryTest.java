package com.library.core.repository;

import com.library.client.NaverClient;
import com.library.client.request.NaverBookSortType;
import com.library.client.response.NaverBookResponse;
import com.library.core.domain.Book;
import com.library.core.support.Page;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class NaverBookRepositoryTest {

    @Mock
    NaverClient naverClient;

    @Test
    void search() {
        given(naverClient.search(anyString(), anyInt(), anyInt(), any(NaverBookSortType.class)))
                .willReturn(new NaverBookResponse(
                        "", 10L, 1L, 10L, List.of()
                ));
        NaverBookRepository naverBookRepository = new NaverBookRepository(naverClient);
        Page<Book> paged = naverBookRepository.search(
                "HTTP 완벽 가이드",
                1,
                10,
                "date"
        );

        assertThat(paged.total()).isEqualTo(10);
    }
}