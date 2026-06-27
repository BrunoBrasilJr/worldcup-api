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

    // Liga/desliga o scheduler pelo application.properties (true por padrao).
    @Value("${ingestion.enabled:true}")
    private boolean enabled;

    /**
     * Roda a ingestao automaticamente a cada 30 minutos (1.800.000 ms).
     * initialDelay = 60.000 ms -> espera 1 min apos subir antes da 1a execucao
     * (evita disparar logo no boot enquanto o app ainda esta inicializando).
     *
     * Cada execucao consome 1 requisicao da quota diaria (max ~48/dia se rodar 24h).
     */
    @Scheduled(fixedRate = 1_800_000, initialDelay = 60_000)
    public void scheduledIngestion() {
        if (!enabled) {
            return;
        }
        System.out.println(">>> [Scheduler] Iniciando ingestao automatica...");
        try {
            int processed = ingestionService.ingestLiveFixtures();
            System.out.println(">>> [Scheduler] Ingestao automatica: " + processed + " jogos.");
        } catch (Exception e) {
            System.out.println(">>> [Scheduler] Erro na ingestao: " + e.getMessage());
        }
    }
}