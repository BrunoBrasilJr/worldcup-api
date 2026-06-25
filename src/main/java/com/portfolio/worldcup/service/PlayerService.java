package com.portfolio.worldcup.service;

import com.portfolio.worldcup.entity.Player;
import com.portfolio.worldcup.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    // Lista todos os jogadores.
    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    // Lista jogadores de um time especifico.
    public List<Player> findByTeam(Long teamId) {
        return playerRepository.findByTeamId(teamId);
    }
}