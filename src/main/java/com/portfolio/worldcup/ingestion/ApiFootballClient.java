package com.portfolio.worldcup.ingestion;

import com.portfolio.worldcup.ingestion.dto.ApiFixturesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class ApiFootballClient {

    private final RestClient apiFootballRestClient;

    /**
     * /status - valida conexao e quota. NAO conta na quota diaria.
     */
    public String getStatus() {
        return apiFootballRestClient.get()
                .uri("/status")
                .retrieve()
                .body(String.class);
    }

    /**
     * /fixtures?live=all - todos os jogos ao vivo agora, JA convertidos no nosso DTO.
     * O RestClient cuida da desserializacao do JSON sozinho.
     * CUSTA 1 requisicao da quota diaria.
     */
    public ApiFixturesResponse getLiveFixtures() {
        return apiFootballRestClient.get()
                .uri("/fixtures?live=all")
                .retrieve()
                .body(ApiFixturesResponse.class);
    }
}