package com.portfolio.worldcup.repository;

import com.portfolio.worldcup.entity.EventType;
import com.portfolio.worldcup.entity.MatchEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchEventRepository extends JpaRepository<MatchEvent, Long> {

    // Todos os eventos de um jogo, ordenados pelo minuto (timeline da partida).
    List<MatchEvent> findByMatchIdOrderByMinuteAsc(Long matchId);

    // Todos os eventos de um tipo (ex: todos os GOAL) -> base p/ artilheiros.
    List<MatchEvent> findByType(EventType type);
}