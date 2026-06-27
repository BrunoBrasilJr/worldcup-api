package com.portfolio.worldcup.controller;

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

    /**
     * Dispara a ingestao dos jogos ao vivo: busca na API-Football e salva no banco.
     * CONSOME 1 requisicao da quota. Use com parcimonia.
     */
    @GetMapping("/ingest-live")
    public Map<String, Object> ingestLive() {
        int processed = ingestionService.ingestLiveFixtures();
        return Map.of(
            "status", "ok",
            "matchesProcessed", processed
        );
    }
}