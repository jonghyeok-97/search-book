package com.library.client;

import com.library.client.request.NaverBookSortType;
import com.library.client.response.NaverBookResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NaverClientTest {

    @Test
    void searchBook() {
        NaverClient naverClient = new NaverClient(new NaverClientStub());
        NaverBookResponse response = naverClient.search("HTTP", 1, 10, NaverBookSortType.SIM);

        assertAll(
                () -> assertEquals(100L, response.total()),
                () -> assertEquals(1L, response.start()),
                () -> assertEquals(10L, response.display())
        );
    }

    static class NaverClientStub implements NaverFeignClient {
        @Override
        public NaverBookResponse searchBook(String query, Integer display, Integer start, NaverBookSortType sort) {
            return new NaverBookResponse(
                    "2022-03-05T11:23:00",
                    100L,
                    1L,
                    10L,
                    List.of()
            );
        }
    }
}