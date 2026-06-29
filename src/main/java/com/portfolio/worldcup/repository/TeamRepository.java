package com.portfolio.worldcup.repository;

import com.portfolio.worldcup.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    // Busca um time pela identidade externa composta (provider + externalId).
    Optional<Team> findByProviderAndExternalId(String provider, Long externalId);
}