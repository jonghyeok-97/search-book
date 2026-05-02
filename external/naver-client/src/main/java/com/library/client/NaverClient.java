package com.library.client;

import com.library.client.request.NaverBookSortType;
import com.library.client.response.NaverBookResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "naverClient", url = "${external.naver.url}", configuration = NaverClientConfiguration.class)
public interface NaverClient {

    @GetMapping("/v1/search/book.json")
    NaverBookResponse searchBook(
            @RequestParam String query,
            @RequestParam(required = false) Integer display,
            @RequestParam(required = false) Integer start,
            @RequestParam(required = false) NaverBookSortType sort
    );
}
