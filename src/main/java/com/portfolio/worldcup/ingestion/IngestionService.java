package com.portfolio.worldcup.ingestion;

import com.portfolio.worldcup.ingestion.dto.ApiFixtureItem;
import com.portfolio.worldcup.ingestion.dto.ApiFixturesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IngestionService {

    private final ApiFootballClient apiFootballClient;
    private final FixtureProcessor fixtureProcessor;

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
     * Processa a resposta da API. Cada partida e salva em sua PROPRIA transacao
     * (via FixtureProcessor). Se uma falhar, as demais continuam.
     */
    private int ingest(ApiFixturesResponse parsed, String origem) {
        if (parsed == null || parsed.response == null || parsed.response.isEmpty()) {
            System.out.println(">>> Nenhum jogo encontrado (" + origem + ").");
            return 0;
        }

        int count = 0;
        int errors = 0;
        for (ApiFixtureItem item : parsed.response) {
            try {
                fixtureProcessor.process(item);   // transacao independente por partida
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