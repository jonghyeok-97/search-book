package com.library.client;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.library.client.response.NaverErrorResponse;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class NaverClientConfiguration {

    @Bean
    public RequestInterceptor naverClientInterceptor(
            @Value("${external.naver.client-id}") String clientId,
            @Value("${external.naver.client-secret}") String secretKey) {
        return template -> {
            template.header("x-naver-client-id", clientId);
            template.header("x-naver-client-secret", secretKey);
        };
    }

    @Bean
    public ErrorDecoder naverErrorDecoder(ObjectMapper objectMapper) {
        return (methodKey, response) -> {
            try {
                String body = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
                NaverErrorResponse errorResponse = objectMapper.readValue(body, NaverErrorResponse.class);
                return new NaverClientCallException(errorResponse.errorMessage());
            } catch (IOException e) {
                log.error("[NaverClient.errorDecoder] JSON 역직렬화 오류: {} {}", methodKey, e.getMessage());
                return new NaverClientCallException("Naver Error Response 역직렬화 중 에러", e);
            }
        };
    }

    @Bean
    public Retryer naverRetryer() {
        return new Retryer.Default(3000, 5000, 3);
    }
}
