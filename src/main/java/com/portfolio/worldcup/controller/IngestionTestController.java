package com.portfolio.worldcup.controller;

import com.portfolio.worldcup.ingestion.ApiFootballClient;
import com.portfolio.worldcup.ingestion.IngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class IngestionTestController {

    private final IngestionService ingestionService;
    private final ApiFootballClient apiFootballClient;

    /** Competicao filtrada (liga+season). Requer acesso a season. */
    @GetMapping("/ingest-competition")
    public Map<String, Object> ingestCompetition() {
        int processed = ingestionService.ingestCompetition();
        return Map.of("status", "ok", "matchesProcessed", processed);
    }

    /** Ao vivo filtrado por liga. */
    @GetMapping("/ingest-live")
    public Map<String, Object> ingestLive() {
        int processed = ingestionService.ingestLive();
        return Map.of("status", "ok", "matchesProcessed", processed);
    }

    /** Ao vivo do mundo (live=all) - funciona no plano free, pega a Copa quando ao vivo. */
    @GetMapping("/ingest-live-all")
    public Map<String, Object> ingestLiveAll() {
        int processed = ingestionService.ingestLiveAll();
        return Map.of("status", "ok", "matchesProcessed", processed);
    }

    /** DEBUG: resposta crua da competicao configurada. */
    @GetMapping("/debug-competition")
    public String debugCompetition() {
        return apiFootballClient.getCompetitionFixturesRaw();
    }
}