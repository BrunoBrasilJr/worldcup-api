package com.portfolio.worldcup.repository;

import com.portfolio.worldcup.entity.Match;
import com.portfolio.worldcup.entity.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    // Jogos com um status especifico (ex: todos os LIVE).
    List<Match> findByStatus(MatchStatus status);

    // Jogos com status dentro de uma lista (ex: LIVE, HALFTIME, EXTRA_TIME, PENALTIES = "ao vivo").
    List<Match> findByStatusIn(List<MatchStatus> statuses);

    // Jogos cuja data/hora esta entre dois instantes (usado para "jogos de hoje").
    List<Match> findByMatchDateTimeBetween(LocalDateTime start, LocalDateTime end);
}