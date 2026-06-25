package com.portfolio.worldcup.repository;

import com.portfolio.worldcup.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    // Busca todos os jogadores de um time, pelo id do time.
    // O Spring le o nome do metodo e gera o SQL: WHERE team_id = ?
    List<Player> findByTeamId(Long teamId);
}