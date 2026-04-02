package com.library.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.common.exception.CoreApiException;
import com.library.client.error.NaverErrorResponse;
import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.RetryableException;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NaverClientConfigurationTest {

    NaverClientConfiguration configuration = new NaverClientConfiguration();

    @Mock
    ObjectMapper objectMapper;

    @Test
    @DisplayName("clientId가 x-naver-client-id 헤더로 설정된다")
    void clientId_is_set_as_header() {
        // given
        String clientId = "test-client-id";
        RequestInterceptor interceptor = configuration.naverClientInterceptor(clientId, "any-secret");
        RequestTemplate template = new RequestTemplate();

        // when
        interceptor.apply(template);

        // then
        assertThat(template.headers().get("x-naver-client-id")).containsExactly(clientId);
    }

    @Test
    @DisplayName("secretKey가 x-naver-client-secret 헤더로 설정된다")
    void secretKey_is_set_as_header() {
        // given
        String secretKey = "test-secret-key";
        RequestInterceptor interceptor = configuration.naverClientInterceptor("any-client", secretKey);
        RequestTemplate template = new RequestTemplate();

        // when
        interceptor.apply(template);

        // then
        assertThat(template.headers().get("x-naver-client-secret")).containsExactly(secretKey);
    }

    @Test
    @DisplayName("4xx 에러 시 CoreApiException를 반환한다")
    void returns_core_api_exception_on_4xx() throws Exception {
        // given
        when(objectMapper.readValue(any(String.class), eq(NaverErrorResponse.class)))
                .thenReturn(new NaverErrorResponse("SE01", "잘못된 요청입니다"));
        ErrorDecoder errorDecoder = configuration.naverErrorDecoder(objectMapper);
        Response response = buildResponse(400, "Bad Request", """
                    {
                        "errorCode": "SE01",
                        "message":"잘못된 요청입니다"
                    }
                """);

        // when
        Exception result = errorDecoder.decode("NaverClient#searchBook", response);

        // then
        assertThat(result).isInstanceOf(CoreApiException.class);
    }

    @Test
    @DisplayName("5xx 에러 시 CoreApiException를 반환한다")
    void returns_core_api_exception_on_5xx() throws Exception {
        // given
        when(objectMapper.readValue(any(String.class), eq(NaverErrorResponse.class)))
                .thenReturn(new NaverErrorResponse("SE02", "서버 에러입니다"));
        ErrorDecoder errorDecoder = configuration.naverErrorDecoder(objectMapper);
        Response response = buildResponse(500, "Internal Server Error", "{\"errorCode\":\"SE02\",\"message\":\"서버 에러입니다\"}");

        // when
        Exception result = errorDecoder.decode("NaverClient#searchBook", response);

        // then
        assertThat(result).isInstanceOf(CoreApiException.class);
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

    private Response buildResponse(int status, String reason, String body) {
        Request request = Request.create(
                Request.HttpMethod.GET,
                "/v1/search/book.json",
                Collections.emptyMap(),
                Request.Body.empty(),
                new RequestTemplate()
        );
        return Response.builder()
                .status(status)
                .reason(reason)
                .request(request)
                .headers(Collections.emptyMap())
                .body(body, StandardCharsets.UTF_8)
                .build();
    }
}
