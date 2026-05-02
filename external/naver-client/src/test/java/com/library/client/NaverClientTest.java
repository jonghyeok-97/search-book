package com.library.client;

import com.library.client.response.NaverBookResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Disabled("로컬에서 환경변수 세팅 후 수동 실행")
@ActiveProfiles("naver-client")
@SpringBootTest(classes = NaverClientTest.Config.class)
public class NaverClientTest {

    @EnableAutoConfiguration
    static class Config {
    }

    @Autowired
    NaverClient naverClient;

    @Test
    void success_example() {
        NaverBookResponse response = naverClient.searchBook("http", null, null, null);

        assertThat(response).isNotNull();
    }

    @Test
    void bad_request_example() {
        assertThatThrownBy(() -> naverClient.searchBook(null, null, null, null))
                .isInstanceOf(NaverClientCallException.class);
    }
}