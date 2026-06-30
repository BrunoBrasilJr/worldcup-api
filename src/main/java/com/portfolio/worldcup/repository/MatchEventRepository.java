package com.portfolio.worldcup.repository;

import com.portfolio.worldcup.entity.EventType;
import com.portfolio.worldcup.entity.MatchEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchEventRepository extends JpaRepository<MatchEvent, Long> {

    List<MatchEvent> findByMatchIdOrderByMinuteAsc(Long matchId);

    List<MatchEvent> findByType(EventType type);

    // Remove todos os eventos de um jogo (usado antes de re-ingerir, evita duplicar).
    void deleteByMatchId(Long matchId);
}