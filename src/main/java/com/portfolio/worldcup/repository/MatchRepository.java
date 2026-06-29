package com.portfolio.worldcup.repository;

import com.portfolio.worldcup.entity.Match;
import com.portfolio.worldcup.entity.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByStatus(MatchStatus status);

    List<Match> findByStatusIn(List<MatchStatus> statuses);

    List<Match> findByMatchDateTimeBetween(LocalDateTime start, LocalDateTime end);

    // Busca um jogo pela identidade externa composta (provider + externalId).
    Optional<Match> findByProviderAndExternalId(String provider, Long externalId);
}