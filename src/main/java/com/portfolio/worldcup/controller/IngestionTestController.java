package com.portfolio.worldcup.controller;

import com.portfolio.worldcup.ingestion.ApiFootballClient;
import com.portfolio.worldcup.ingestion.IngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class IngestionTestController {

    private final IngestionService ingestionService;
    private final ApiFootballClient apiFootballClient;

    @GetMapping("/ingest-competition")
    public Map<String, Object> ingestCompetition() {
        int processed = ingestionService.ingestCompetition();
        return Map.of("status", "ok", "matchesProcessed", processed);
    }

    @GetMapping("/ingest-live")
    public Map<String, Object> ingestLive() {
        int processed = ingestionService.ingestLive();
        return Map.of("status", "ok", "matchesProcessed", processed);
    }

    @GetMapping("/ingest-live-all")
    public Map<String, Object> ingestLiveAll() {
        int processed = ingestionService.ingestLiveAll();
        return Map.of("status", "ok", "matchesProcessed", processed);
    }

    @GetMapping("/ingest-events/{matchId}")
    public Map<String, Object> ingestEvents(@PathVariable Long matchId) {
        int saved = ingestionService.ingestEventsForMatch(matchId);
        return Map.of("status", saved >= 0 ? "ok" : "not_found", "eventsPersisted", saved);
    }

    /**
     * Dispara a sincronizacao de eventos dos jogos elegiveis (rotacao justa),
     * com teto de 'max' jogos. Util para forcar a sincronizacao sob demanda.
     * CUSTA ate 'max' requisicoes.
     */
    @GetMapping("/sync-active-events")
    public Map<String, Object> syncActiveEvents(@RequestParam(defaultValue = "3") int max) {
        int synced = ingestionService.ingestEventsForActiveMatches(max);
        return Map.of("status", "ok", "matchesSynced", synced, "limit", max);
    }

    @GetMapping("/debug-competition")
    public String debugCompetition() {
        return apiFootballClient.getCompetitionFixturesRaw();
    }
}