package com.library.client;

import com.library.client.request.KakaoBookSearchType;
import com.library.client.request.KakaoBookSortType;
import com.library.client.response.KakaoBookResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("로컬에서 환경변수 세팅 후 수동 실행")
@ActiveProfiles("kakao-client")
@SpringBootTest(classes = KakaoClientTest.Config.class)
public class KakaoClientTest {
    private static final Logger log = LoggerFactory.getLogger(KakaoClientTest.class);

    @EnableAutoConfiguration
    static class Config {
    }

    @Autowired
    KakaoClient kakaoClient;

    @Test
    void success_example() {
        KakaoBookResponse response = kakaoClient.searchBook(
                "http",
                KakaoBookSortType.ACCURACY,
                1,
                10,
                KakaoBookSearchType.TITLE);

        assertThat(response).isNotNull();

        log.debug(response.meta().toString());
        response.documents().forEach(document -> log.debug("{}", document.toString()));
    }
}
