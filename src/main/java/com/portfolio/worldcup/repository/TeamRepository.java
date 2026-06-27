package com.portfolio.worldcup.repository;

import com.portfolio.worldcup.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    // Busca um time pelo ID externo (da API-Football). Usado na ingestao p/ evitar duplicar.
    Optional<Team> findByExternalId(Long externalId);
}