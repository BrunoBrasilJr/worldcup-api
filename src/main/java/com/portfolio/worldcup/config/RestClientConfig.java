package com.portfolio.worldcup.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${apifootball.base-url}")
    private String baseUrl;

    @Value("${apifootball.key}")
    private String apiKey;

    @Bean
    public RestClient apiFootballRestClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                // O header de autenticacao da API-Football: x-apisports-key
                .defaultHeader("x-apisports-key", apiKey)
                .build();
    }
}