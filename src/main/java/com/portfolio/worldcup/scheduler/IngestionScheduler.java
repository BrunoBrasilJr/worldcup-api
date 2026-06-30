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

    @Value("${ingestion.events.max-matches-per-cycle:3}")
    private int maxMatchesPerCycle;

    /**
     * SCHEDULER DE JOGOS: atualiza placar/status (live=all).
     * Barato: 1 requisicao traz todos os jogos ao vivo.
     */
    @Scheduled(fixedRateString = "${ingestion.fixtures.interval-ms:1800000}", initialDelay = 60_000)
    public void scheduledFixtures() {
        if (!enabled) {
            log.debug("[Scheduler/Jogos] desabilitado (ingestion.enabled=false).");
            return;
        }
        log.info("[Scheduler/Jogos] Atualizando jogos ao vivo...");
        try {
            int processed = ingestionService.ingestLiveAll();
            log.info("[Scheduler/Jogos] Concluido: {} jogos.", processed);
        } catch (Exception e) {
            log.error("[Scheduler/Jogos] Erro: {}", e.getMessage(), e);
        }
    }

    /**
     * SCHEDULER DE EVENTOS: sincroniza eventos dos jogos elegiveis,
     * com rotacao justa e teto de 'maxMatchesPerCycle' jogos por ciclo.
     * Cada jogo custa 1 requisicao (cuidado com quota).
     */
    @Scheduled(fixedRateString = "${ingestion.events.interval-ms:600000}", initialDelay = 120_000)
    public void scheduledEvents() {
        if (!enabled) {
            log.debug("[Scheduler/Eventos] desabilitado (ingestion.enabled=false).");
            return;
        }
        log.info("[Scheduler/Eventos] Sincronizando eventos (ate {} jogos)...", maxMatchesPerCycle);
        try {
            int synced = ingestionService.ingestEventsForActiveMatches(maxMatchesPerCycle);
            log.info("[Scheduler/Eventos] Concluido: {} jogos sincronizados.", synced);
        } catch (Exception e) {
            log.error("[Scheduler/Eventos] Erro: {}", e.getMessage(), e);
        }
    }
}