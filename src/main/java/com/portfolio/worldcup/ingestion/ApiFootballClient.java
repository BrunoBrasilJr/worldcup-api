package com.portfolio.worldcup.ingestion;

import com.portfolio.worldcup.ingestion.dto.ApiEventsResponse;
import com.portfolio.worldcup.ingestion.dto.ApiFixturesResponse;
import com.portfolio.worldcup.ingestion.exception.QuotaExceededException;
import com.portfolio.worldcup.ingestion.exception.TransientApiException;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class ApiFootballClient {

    private static final String RETRY_NAME = "apiFootball";

    private final RestClient apiFootballRestClient;

    @Value("${apifootball.league}")
    private int league;

    @Value("${apifootball.season}")
    private int season;

    @Retry(name = RETRY_NAME)
    public String getStatus() {
        return execute(() -> apiFootballRestClient.get()
                .uri("/status")
                .retrieve()
                .body(String.class));
    }

    @Retry(name = RETRY_NAME)
    public ApiFixturesResponse getCompetitionFixtures() {
        return execute(() -> apiFootballRestClient.get()
                .uri(b -> b.path("/fixtures")
                        .queryParam("league", league)
                        .queryParam("season", season)
                        .build())
                .retrieve()
                .body(ApiFixturesResponse.class));
    }

    @Retry(name = RETRY_NAME)
    public String getCompetitionFixturesRaw() {
        return execute(() -> apiFootballRestClient.get()
                .uri(b -> b.path("/fixtures")
                        .queryParam("league", league)
                        .queryParam("season", season)
                        .build())
                .retrieve()
                .body(String.class));
    }

    @Retry(name = RETRY_NAME)
    public ApiFixturesResponse getLiveCompetitionFixtures() {
        return execute(() -> apiFootballRestClient.get()
                .uri(b -> b.path("/fixtures")
                        .queryParam("live", league)
                        .build())
                .retrieve()
                .body(ApiFixturesResponse.class));
    }

    @Retry(name = RETRY_NAME)
    public ApiFixturesResponse getLiveFixtures() {
        return execute(() -> apiFootballRestClient.get()
                .uri("/fixtures?live=all")
                .retrieve()
                .body(ApiFixturesResponse.class));
    }

    @Retry(name = RETRY_NAME)
    public ApiEventsResponse getEvents(Long fixtureExternalId) {
        return execute(() -> apiFootballRestClient.get()
                .uri(b -> b.path("/fixtures/events")
                        .queryParam("fixture", fixtureExternalId)
                        .build())
                .retrieve()
                .body(ApiEventsResponse.class));
    }

    /**
     * Executa a chamada e traduz falhas em excecoes que controlam o retry:
     * - timeout / conexao (ResourceAccessException, IOException) -> TransientApiException (re-tenta)
     * - o tratamento de status HTTP (5xx, 429, 4xx) e feito no errorHandler do RestClient (config).
     */
    private <T> T execute(Supplier<T> call) {
        try {
            return call.get();
        } catch (ResourceAccessException e) {
            // Falha de I/O (timeout, conexao recusada): transitorio.
            throw new TransientApiException("Falha de conexao/timeout na API-Football", e);
        } catch (TransientApiException | QuotaExceededException e) {
            // Ja classificadas pelo errorHandler: repassa.
            throw e;
        } catch (Exception e) {
            // Demais erros (ex: 4xx) sao tratados como permanentes pelo errorHandler;
            // se chegar algo aqui sem classificacao, considera permanente (nao re-tenta).
            throw e;
        }
    }

    // Helpers usados pelo errorHandler (configurado no RestClient) para classificar status.
    public static boolean isTransientStatus(HttpStatusCode status) {
        int code = status.value();
        return code == 500 || code == 502 || code == 503 || code == 504;
    }
}