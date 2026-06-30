package com.portfolio.worldcup.repository;

import com.portfolio.worldcup.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findByTeamId(Long teamId);

    // Busca um jogador pelo nome (usado na ingestao de eventos, que so traz nome).
    Optional<Player> findFirstByName(String name);
}