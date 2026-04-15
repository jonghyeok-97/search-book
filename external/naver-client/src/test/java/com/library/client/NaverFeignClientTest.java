package com.library.client;

import com.library.client.response.NaverBookResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Disabled
@SpringBootTest(classes = NaverFeignClientTest.Config.class)
public class NaverFeignClientTest {

    @EnableAutoConfiguration
    @EnableFeignClients(clients = {NaverFeignClient.class})
    static class Config {
    }

    @Autowired
    NaverFeignClient naverFeignClient;

    @Test
    void success_example() {
        NaverBookResponse response = naverFeignClient.searchBook("http", null, null, null);

        assertThat(response).isNotNull();
    }

    @Test
    void bad_request_example() {
        assertThatThrownBy(() -> naverFeignClient.searchBook(null, null, null, null))
                .isInstanceOf(NaverClientCallException.class);
    }
}
