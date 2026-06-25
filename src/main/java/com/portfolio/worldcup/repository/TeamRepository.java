package com.portfolio.worldcup.repository;

import com.portfolio.worldcup.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
    // JpaRepository<Team, Long> = repositorio da entidade Team, cujo id e do tipo Long.
    // Ja vem de graca: save, findById, findAll, deleteById, count, etc.
}