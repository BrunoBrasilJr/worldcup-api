package com.portfolio.worldcup.config;

import com.portfolio.worldcup.ingestion.exception.QuotaExceededException;
import com.portfolio.worldcup.ingestion.exception.TransientApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
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
                // Header de autenticacao da API-Football.
                .defaultHeader("x-apisports-key", apiKey)
                // Classifica respostas de erro para guiar a politica de retry.
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                    int status = response.getStatusCode().value();

                    // 429 - Too Many Requests
                    if (status == 429) {
                        String retryAfter = response.getHeaders().getFirst("Retry-After");
                        if (retryAfter != null && !retryAfter.isBlank()) {
                            // Tem Retry-After: e transitorio, vale re-tentar (respeitando a espera).
                            throw new TransientApiException(
                                "429 Too Many Requests (Retry-After=" + retryAfter + ")");
                        }
                        // Sem Retry-After: quota esgotada, nao insiste agora.
                        throw new QuotaExceededException(
                            "429 Too Many Requests - quota esgotada (sem Retry-After)");
                    }

                    // 5xx transitorios: vale re-tentar.
                    if (status == 500 || status == 502 || status == 503 || status == 504) {
                        throw new TransientApiException("Erro transitorio do servidor: HTTP " + status);
                    }

                    // Demais erros (400, 401, 403, 404, outros 5xx): permanentes, nao re-tenta.
                    throw new RuntimeException("Erro permanente da API: HTTP " + status);
                })
                .build();
    }
}