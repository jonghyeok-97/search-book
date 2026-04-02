package com.library.client;

import com.library.client.response.NaverBookResponse;
import com.library.common.exception.CoreApiException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = NaverClientTest.Config.class)
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
class NaverClientTest {

    @EnableAutoConfiguration
    @EnableFeignClients(clients = {NaverClient.class})
    static class Config {
    }

    @Autowired
    NaverClient naverClient;

    @Test
    void connect() {
        stubFor(post("/v1/search/book.json")
                .willReturn(okJson("""
                                {"lastBuildDate":"...","total":1,"start":1,"display":1,"items":[]}
                """)));
        NaverBookResponse response = naverClient.searchBook("http", null, null, null);

        assertThat(response).isNotNull();
    }

    @Test
    void fail() {
        stubFor(post("/v1/search/book.json")
                .willReturn(badRequest().withBody("""
                            {"errorCode": "SE01", "errorMessage": "잘못된요청"}
                        """)));

        assertThatThrownBy(() -> naverClient.searchBook(null, null, null, null))
                .isInstanceOf(CoreApiException.class);
    }
}
