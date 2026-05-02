package com.library.client;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@AutoConfiguration
@EnableFeignClients(clients = NaverClient.class)
public class NaverClientAutoConfiguration {
}
