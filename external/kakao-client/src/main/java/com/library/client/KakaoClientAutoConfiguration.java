package com.library.client;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@AutoConfiguration
@EnableFeignClients(clients = KakaoClient.class)
public class KakaoClientAutoConfiguration {
}

