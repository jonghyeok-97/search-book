package com.library.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.library.client.response.KakaoErrorResponse;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class KakaoClientConfiguration {

    @Bean
    public RequestInterceptor kakaoClientInterceptor(
            @Value("${external.kakao.rest-api-key}") String restApiKey
    ) {
        return template -> template.header(HttpHeaders.AUTHORIZATION, restApiKey);
    }

    @Bean
    public ErrorDecoder kakaoErrorDecoder(ObjectMapper objectMapper) {
        return (methodKey, response) -> {
            try {
                String body = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
                KakaoErrorResponse errorResponse = objectMapper.readValue(body, KakaoErrorResponse.class);
                return new KakaoClientCallException(errorResponse.message());
            } catch (IOException e) {
                log.error("[KakaoClient.errorDecoder] JSON 역직렬화 오류: {} {}", methodKey, e.getMessage());
                return new KakaoClientCallException("Kakao Error Response 역직렬화 중 에러", e);
            }
        };
    }

    @Bean
    public Decoder feignDecoder() {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper);

        return new ResponseEntityDecoder(new SpringDecoder(() -> new HttpMessageConverters(jacksonConverter)));
    }

    @Bean
    public Retryer kakaoRetryer() {
        return new Retryer.Default(3000, 5000, 3);
    }
}
