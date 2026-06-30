package com.portfolio.worldcup.repository;

import com.portfolio.worldcup.entity.Match;
import com.portfolio.worldcup.entity.MatchStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByStatus(MatchStatus status);

    List<Match> findByStatusIn(List<MatchStatus> statuses);

    List<Match> findByMatchDateTimeBetween(LocalDateTime start, LocalDateTime end);

    Optional<Match> findByProviderAndExternalId(String provider, Long externalId);

    /**
     * Busca jogos ELEGIVEIS para sincronizacao de eventos, em ordem de ROTACAO JUSTA:
     * - Elegiveis: status ativo (LIVE, HALFTIME, EXTRA_TIME, PENALTIES)
     *   OU finalizados que ainda nao tiveram eventos ingeridos (captura final).
     * - Ordem: lastEventsSync ASC com NULLS FIRST (nunca sincronizado vem primeiro;
     *   depois, o que esta ha mais tempo sem sincronizar).
     *
     * O Pageable limita a N jogos por ciclo (teto de quota).
     */
    @Query("""
            SELECT m FROM Match m
            WHERE m.status IN (
                com.portfolio.worldcup.entity.MatchStatus.LIVE,
                com.portfolio.worldcup.entity.MatchStatus.HALFTIME,
                com.portfolio.worldcup.entity.MatchStatus.EXTRA_TIME,
                com.portfolio.worldcup.entity.MatchStatus.PENALTIES
            )
            OR (m.status = com.portfolio.worldcup.entity.MatchStatus.FINISHED
                AND m.eventsIngested = false)
            ORDER BY m.lastEventsSync ASC NULLS FIRST
            """)
    List<Match> findEligibleForEventSync(Pageable pageable);
}