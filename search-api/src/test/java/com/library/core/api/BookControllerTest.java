package com.library.core.api;

import com.library.core.domain.BookService;
import com.library.core.support.Page;
import com.library.core.support.exception.CoreApiException;
import com.library.core.support.exception.ErrorType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
    BookService bookService;

    @Test
    void success_200_OK() throws Exception {
        given(bookService.search(anyString(), anyInt(), anyInt(), any()))
                .willReturn(Page.from(
                        10,
                        anyList()
                ));

        mockMvc.perform(get("/v1/books")
                        .queryParam("query", "HTTP")
                        .queryParam("page", "1")
                        .queryParam("size", "10")
                        .queryParam("sort", "SIM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.content[0].title").value("HTTP 완벽 가이드"));
    }

    @Test
    void 외부_API_오류시_500_반환() throws Exception {
        given(bookService.search(anyString(), anyInt(), anyInt(), any()))
                .willThrow(new CoreApiException(ErrorType.EXTERNAL_API_ERROR));

        mockMvc.perform(get("/v1/books")
                        .param("query", "HTTP")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sort", "DATE"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("외부 API 호출 에러입니다"));
    }

    @Test
    void query가_비어있을때_400응답이_반환된다() throws Exception {
        mockMvc.perform(get("/v1/books")
                        .queryParam("query", " ")
                        .queryParam("page", "1")
                        .queryParam("size", "10")
                        .queryParam("sort", "SIM"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("query는 필수 값입니다."));
    }
}
