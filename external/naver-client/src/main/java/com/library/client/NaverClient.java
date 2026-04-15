package com.library.client;

import com.library.client.request.NaverBookSortType;
import com.library.client.response.NaverBookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NaverClient {
    private final NaverFeignClient naverFeignClient;

    public NaverBookResponse search(String query, int page, int size, NaverBookSortType sort) {
        return naverFeignClient.searchBook(query, page, size, sort);
    }
}
