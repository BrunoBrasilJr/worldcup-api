package com.portfolio.worldcup.ingestion;

import com.portfolio.worldcup.entity.Match;
import com.portfolio.worldcup.ingestion.dto.ApiEventsResponse;
import com.portfolio.worldcup.ingestion.dto.ApiFixtureItem;
import com.portfolio.worldcup.ingestion.dto.ApiFixturesResponse;
import com.portfolio.worldcup.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IngestionService {

    private final ApiFootballClient apiFootballClient;
    private final FixtureProcessor fixtureProcessor;
    private final EventProcessor eventProcessor;
    private final MatchRepository matchRepository;

    public int ingestCompetition() {
        return ingest(apiFootballClient.getCompetitionFixtures(), "competicao completa");
    }

    public int ingestLive() {
        return ingest(apiFootballClient.getLiveCompetitionFixtures(), "ao vivo da competicao");
    }

    public int ingestLiveAll() {
        return ingest(apiFootballClient.getLiveFixtures(), "ao vivo (mundo)");
    }

    public int ingestEventsForMatch(Long matchId) {
        Match match = matchRepository.findById(matchId).orElse(null);
        if (match == null) {
            log.warn("Jogo {} nao encontrado para ingestao de eventos.", matchId);
            return -1;
        }
        if (match.getExternalId() == null) {
            log.warn("Jogo {} nao tem externalId (nao veio da API). Ingestao de eventos abortada.", matchId);
            return -1;
        }

        ApiEventsResponse events = apiFootballClient.getEvents(match.getExternalId());
        int saved = eventProcessor.processEvents(match, events);
        log.info("Eventos ingeridos para o jogo {}: {} eventos.", matchId, saved);
        return saved;
    }

    /**
     * Sincroniza eventos dos jogos ELEGIVEIS, com ROTACAO JUSTA e teto de quota.
     * Busca os 'maxMatches' jogos ha mais tempo sem sincronizar (NULLS primeiro),
     * e ingere eventos de cada um. Cada jogo custa 1 requisicao.
     * Retorna quantos jogos foram efetivamente sincronizados.
     */
    public int ingestEventsForActiveMatches(int maxMatches) {
        if (maxMatches <= 0) {
            return 0;
        }

        List<Match> eligible = matchRepository.findEligibleForEventSync(PageRequest.of(0, maxMatches));
        if (eligible.isEmpty()) {
            log.debug("Nenhum jogo elegivel para sincronizacao de eventos neste ciclo.");
            return 0;
        }

        int synced = 0;
        for (Match match : eligible) {
            if (match.getExternalId() == null) {
                continue;
            }
            try {
                ApiEventsResponse events = apiFootballClient.getEvents(match.getExternalId());
                eventProcessor.processEvents(match, events);
                synced++;
            } catch (Exception e) {
                log.error("Erro ao sincronizar eventos do jogo {}: {}", match.getId(), e.getMessage());
            }
        }

        log.info("Sincronizacao de eventos: {} de {} jogos elegiveis processados.", synced, eligible.size());
        return synced;
    }

    private int ingest(ApiFixturesResponse parsed, String origem) {
        if (parsed == null || parsed.response == null || parsed.response.isEmpty()) {
            log.warn("Nenhum jogo encontrado ({}).", origem);
            return 0;
        }

        int count = 0;
        int errors = 0;
        for (ApiFixtureItem item : parsed.response) {
            try {
                fixtureProcessor.process(item);
                count++;
            } catch (Exception e) {
                errors++;
                log.error("Erro ao processar jogo: {}", e.getMessage(), e);
            }
        }

        log.info("Ingestao ({}) concluida: {} processados, {} com erro.", origem, count, errors);
        return count;
    }
}