package com.library.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.client.response.KakaoErrorResponse;
import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KakaoClientConfigurationTest {

    KakaoClientConfiguration configuration;

    @Mock
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        configuration = new KakaoClientConfiguration();
    }

    @Test
    void applyInterceptor() {
        RequestTemplate template = new RequestTemplate();
        assertThat(template.headers().get(HttpHeaders.AUTHORIZATION)).isNull();
        String givenRestApiKey = "test-rest-api-key";

        RequestInterceptor requestInterceptor = configuration.kakaoClientInterceptor(givenRestApiKey);
        requestInterceptor.apply(template);

        assertThat(template.headers().get(HttpHeaders.AUTHORIZATION)).containsExactly(givenRestApiKey);
    }

    @Test
    void applyErrorDecoder() throws JsonProcessingException {
        // given
        when(objectMapper.readValue(any(String.class), eq(KakaoErrorResponse.class)))
                .thenReturn(new KakaoErrorResponse("AccessDeniedError", "wrong appKey(***) format"));

        Request request = Request.create(
                Request.HttpMethod.GET,
                "localhost:8080",
                Map.of(),
                null,
                StandardCharsets.UTF_8
        );
        Response response = Response.builder()
                .status(401)
                .request(request)
                .body("""
                        {
                            "errorType": "AccessDeniedError",
                            "message": "wrong appKey(***) format"
                        }
                        """, StandardCharsets.UTF_8)
                .build();
        // when
        ErrorDecoder errorDecoder = configuration.kakaoErrorDecoder(objectMapper);
        Exception exception = errorDecoder.decode("methodKey", response);

        // then
        assertThat(exception).isInstanceOf(KakaoClientCallException.class);
        assertThat(exception.getMessage()).isEqualTo("wrong appKey(***) format");
    }

    @Test
    void not_deserialize_while_reading_ErrorDecoder() throws JsonProcessingException {
        // given
        when(objectMapper.readValue(any(String.class), eq(KakaoErrorResponse.class)))
                .thenThrow(JsonProcessingException.class);

        Request request = Request.create(
                Request.HttpMethod.GET,
                "localhost:8080",
                Map.of(),
                null,
                new RequestTemplate()
        );
        Response response = Response.builder()
                .status(401)
                .request(request)
                .body("invalid json", StandardCharsets.UTF_8)
                .build();

        // when
        ErrorDecoder errorDecoder = configuration.kakaoErrorDecoder(objectMapper);
        Exception exception = errorDecoder.decode("methodKey", response);

        // then
        assertThat(exception).isInstanceOf(KakaoClientCallException.class);
        assertThat(exception.getMessage()).isEqualTo("Kakao Error Response 역직렬화 중 에러");
    }
}
