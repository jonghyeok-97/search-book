package com.library.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.library.core", "com.library.client"})
public class SearchApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchApiApplication.class, args);
    }
}
