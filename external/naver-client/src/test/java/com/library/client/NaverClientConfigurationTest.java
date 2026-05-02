package com.library.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.library.client.response.NaverErrorResponse;
import feign.*;
import feign.codec.ErrorDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NaverClientConfigurationTest {

    NaverClientConfiguration configuration;

    @Mock
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        configuration = new NaverClientConfiguration();
    }

    @Test
    void applyInterceptor() {
        RequestTemplate template = new RequestTemplate();
        assertThat(template.headers().get("X-naver-client-id")).isNull();
        assertThat(template.headers().get("X-naver-client-secret")).isNull();

        RequestInterceptor requestInterceptor = configuration.naverClientInterceptor("client-id", "secrey-key");
        requestInterceptor.apply(template);

        assertThat(template.headers().get("X-naver-client-id")).containsExactly("client-id");
        assertThat(template.headers().get("X-naver-client-secret")).containsExactly("secrey-key");
    }

    @Test
    void applyErrorDecoder() throws JsonProcessingException {
        // given
        when(objectMapper.readValue(any(String.class), eq(NaverErrorResponse.class)))
                .thenReturn(new NaverErrorResponse("E401", "미 인증"));

        Request request = Request.create(
                Request.HttpMethod.GET,
                "localhost:8080",
                Map.of(),
                null,
                StandardCharsets.UTF_8
        );
        Response response = Response.builder()
                .status(400)
                .request(request)
                .body("""
                        {
                            "errorCode": "E401",
                            "errorMessage": "미 인증"
                        }
                        """, StandardCharsets.UTF_8)
                .build();
        // when
        ErrorDecoder errorDecoder = configuration.naverErrorDecoder(objectMapper);
        Exception exception = errorDecoder.decode("methodKey", response);

        // then
        assertThat(exception).isInstanceOf(NaverClientCallException.class);
        NaverClientCallException callException = (NaverClientCallException) exception;
        assertThat(callException.getMessage()).isEqualTo("미 인증");
    }

    @Test
    @DisplayName("최대 재시도 횟수(3) 내에서는 예외를 던지지 않는다")
    void does_not_throw_within_max_attempts() {
        // given
        Retryer retryer = configuration.naverRetryer();
        RetryableException exception = buildRetryableException();

        // when & then - 1번째, 2번째는 통과
        retryer.continueOrPropagate(exception);
        retryer.continueOrPropagate(exception);
    }

    @Test
    @DisplayName("최대 재시도 횟수(3) 초과 시 RetryableException을 던진다")
    void throws_exception_when_max_attempts_exceeded() {
        // given
        Retryer retryer = configuration.naverRetryer();
        RetryableException exception = buildRetryableException();

        // when - 2번 소진
        retryer.continueOrPropagate(exception);
        retryer.continueOrPropagate(exception);

        // then - 3번째에 throw
        assertThatThrownBy(() -> retryer.continueOrPropagate(exception))
                .isInstanceOf(RetryableException.class);
    }

    private RetryableException buildRetryableException() {
        Request request = Request.create(
                Request.HttpMethod.GET,
                "/v1/search/book.json",
                Collections.emptyMap(),
                Request.Body.empty(),
                new RequestTemplate()
        );
        return new RetryableException(500, "Server Error", Request.HttpMethod.GET, (Long) null, request);
    }
}
