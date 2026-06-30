package com.portfolio.worldcup.ingestion;

import com.portfolio.worldcup.entity.Match;
import com.portfolio.worldcup.ingestion.dto.ApiEventsResponse;
import com.portfolio.worldcup.ingestion.dto.ApiFixtureItem;
import com.portfolio.worldcup.ingestion.dto.ApiFixturesResponse;
import com.portfolio.worldcup.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IngestionService {

    private final ApiFootballClient apiFootballClient;
    private final FixtureProcessor fixtureProcessor;
    private final EventProcessor eventProcessor;
    private final MatchRepository matchRepository;

    /** Competicao filtrada (liga+season). Requer acesso a season. */
    public int ingestCompetition() {
        return ingest(apiFootballClient.getCompetitionFixtures(), "competicao completa");
    }

    /** Ao vivo filtrado por liga. */
    public int ingestLive() {
        return ingest(apiFootballClient.getLiveCompetitionFixtures(), "ao vivo da competicao");
    }

    /** Ao vivo do mundo (live=all) - funciona no plano free. */
    public int ingestLiveAll() {
        return ingest(apiFootballClient.getLiveFixtures(), "ao vivo (mundo)");
    }

    /**
     * Ingere os EVENTOS de UM jogo (pelo id interno do nosso banco).
     * CUSTA 1 requisicao da quota.
     * Retorna a quantidade de eventos persistidos, ou -1 se o jogo nao existe.
     */
    public int ingestEventsForMatch(Long matchId) {
        Match match = matchRepository.findById(matchId).orElse(null);
        if (match == null) {
            System.out.println(">>> Jogo " + matchId + " nao encontrado.");
            return -1;
        }
        if (match.getExternalId() == null) {
            System.out.println(">>> Jogo " + matchId + " nao tem externalId (nao veio da API).");
            return -1;
        }

        ApiEventsResponse events = apiFootballClient.getEvents(match.getExternalId());
        int saved = eventProcessor.processEvents(match, events);
        System.out.println(">>> Eventos ingeridos para o jogo " + matchId + ": " + saved);
        return saved;
    }

    private int ingest(ApiFixturesResponse parsed, String origem) {
        if (parsed == null || parsed.response == null || parsed.response.isEmpty()) {
            System.out.println(">>> Nenhum jogo encontrado (" + origem + ").");
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
                System.out.println(">>> Erro ao processar jogo: " + e.getMessage());
            }
        }

        System.out.println(">>> Ingestao (" + origem + ") concluida: "
                + count + " processados, " + errors + " com erro.");
        return count;
    }
}