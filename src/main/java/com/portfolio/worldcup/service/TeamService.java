package com.portfolio.worldcup.service;

import com.portfolio.worldcup.entity.Team;
import com.portfolio.worldcup.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    // Lista todos os times.
    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    // Busca um time pelo id (ou null se nao existir).
    public Team findById(Long id) {
        return teamRepository.findById(id).orElse(null);
    }
}