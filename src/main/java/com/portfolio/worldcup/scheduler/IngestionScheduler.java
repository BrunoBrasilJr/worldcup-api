package com.portfolio.worldcup.scheduler;

import com.portfolio.worldcup.ingestion.IngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IngestionScheduler {

    private final IngestionService ingestionService;

    @Value("${ingestion.enabled:true}")
    private boolean enabled;

    /**
     * Ingestao automatica a cada 30 min. Usa live=all (funciona no plano free
     * e captura jogos da Copa quando estao ao vivo).
     */
    @Scheduled(fixedRate = 1_800_000, initialDelay = 60_000)
    public void scheduledIngestion() {
        if (!enabled) {
            return;
        }
        System.out.println(">>> [Scheduler] Iniciando ingestao automatica (live=all)...");
        try {
            int processed = ingestionService.ingestLiveAll();
            System.out.println(">>> [Scheduler] Ingestao automatica: " + processed + " jogos.");
        } catch (Exception e) {
            System.out.println(">>> [Scheduler] Erro na ingestao: " + e.getMessage());
        }
    }
}