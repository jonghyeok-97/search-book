package com.library.core.api;

import com.library.common.exception.CoreApiException;
import com.library.common.exception.ErrorType;
import com.library.core.domain.Book;
import com.library.core.domain.BookSearchService;
import com.library.core.support.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BookSearchService bookSearchService;

    @Test
    void success_200_OK() throws Exception {
        given(bookSearchService.search(anyString(), anyInt(), anyInt(), anyString()))
                .willReturn(Page.from(
                        10,
                        List.of(Book.create("HTTP 완벽 가이드", "데이빗 고울리", "20020101", "1234567890", "HTTP 설명서"))
                ));

        mockMvc.perform(get("/books")
                        .queryParam("query", "HTTP")
                        .queryParam("page", "1")
                        .queryParam("size", "10")
                        .queryParam("sort", "sim"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data[0].title").value("HTTP 완벽 가이드"));
    }

    @Test
    void 외부_API_오류시_500_반환() throws Exception {
        given(bookSearchService.search(anyString(), anyInt(), anyInt(), anyString()))
                .willThrow(new CoreApiException("외부 API 호출 에러입니다", ErrorType.EXTERNAL_API_ERROR));

        mockMvc.perform(get("/books")
                        .param("query", "HTTP")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sort", "date"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("외부 API 호출 에러입니다"));
    }
}
