package com.portfolio.worldcup.ingestion;

import com.portfolio.worldcup.ingestion.dto.ApiEventsResponse;
import com.portfolio.worldcup.ingestion.dto.ApiFixturesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class ApiFootballClient {

    private final RestClient apiFootballRestClient;

    @Value("${apifootball.league}")
    private int league;

    @Value("${apifootball.season}")
    private int season;

    public String getStatus() {
        return apiFootballRestClient.get()
                .uri("/status")
                .retrieve()
                .body(String.class);
    }

    public ApiFixturesResponse getCompetitionFixtures() {
        return apiFootballRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/fixtures")
                        .queryParam("league", league)
                        .queryParam("season", season)
                        .build())
                .retrieve()
                .body(ApiFixturesResponse.class);
    }

    public String getCompetitionFixturesRaw() {
        return apiFootballRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/fixtures")
                        .queryParam("league", league)
                        .queryParam("season", season)
                        .build())
                .retrieve()
                .body(String.class);
    }

    public ApiFixturesResponse getLiveCompetitionFixtures() {
        return apiFootballRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/fixtures")
                        .queryParam("live", league)
                        .build())
                .retrieve()
                .body(ApiFixturesResponse.class);
    }

    public ApiFixturesResponse getLiveFixtures() {
        return apiFootballRestClient.get()
                .uri("/fixtures?live=all")
                .retrieve()
                .body(ApiFixturesResponse.class);
    }

    /**
     * Busca os eventos (gols, cartoes, subst, VAR) de UM jogo, pelo fixture id da API.
     * CUSTA 1 requisicao da quota POR JOGO.
     */
    public ApiEventsResponse getEvents(Long fixtureExternalId) {
        return apiFootballRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/fixtures/events")
                        .queryParam("fixture", fixtureExternalId)
                        .build())
                .retrieve()
                .body(ApiEventsResponse.class);
    }
}