package com.library.client;

import com.library.client.request.KakaoBookSearchType;
import com.library.client.request.KakaoBookSortType;
import com.library.client.response.KakaoBookResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakaoClient", url = "${external.kakao.url}", configuration = KakaoClientConfiguration.class)
public interface KakaoClient {

    @GetMapping("/v3/search/book")
    KakaoBookResponse searchBook(
            @RequestParam String query,
            @RequestParam(required = false) KakaoBookSortType sort,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) KakaoBookSearchType target
    );
}
