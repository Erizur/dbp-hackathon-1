package com.example.oreo.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + System.getenv("GITHUB_TOKEN"))
            .defaultHeader(HttpHeaders.ACCEPT, "application/json")
            .build();
    }
}
