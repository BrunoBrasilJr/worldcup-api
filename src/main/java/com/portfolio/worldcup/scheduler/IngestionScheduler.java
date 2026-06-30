package com.portfolio.worldcup.scheduler;

import com.portfolio.worldcup.ingestion.IngestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class IngestionScheduler {

    private final IngestionService ingestionService;

    @Value("${ingestion.enabled:true}")
    private boolean enabled;

    @Scheduled(fixedRate = 1_800_000, initialDelay = 60_000)
    public void scheduledIngestion() {
        if (!enabled) {
            log.debug("Scheduler de ingestao desabilitado (ingestion.enabled=false).");
            return;
        }
        log.info("[Scheduler] Iniciando ingestao automatica (live=all)...");
        try {
            int processed = ingestionService.ingestLiveAll();
            log.info("[Scheduler] Ingestao automatica concluida: {} jogos.", processed);
        } catch (Exception e) {
            log.error("[Scheduler] Erro na ingestao automatica: {}", e.getMessage(), e);
        }
    }
}