package com.portfolio.worldcup.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID do jogo na API-Football (chave para ingestao idempotente).
    @Column(unique = true)
    private Long externalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;

    @Column(nullable = false)
    private Integer homeScore = 0;

    @Column(nullable = false)
    private Integer awayScore = 0;

    private LocalDateTime matchDateTime;
    private String stadium;
    private String city;

    // Competicao e fase (ex: "FIFA World Cup", "Group Stage").
    private String league;
    private String round;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus status = MatchStatus.SCHEDULED;

    @Column(nullable = false)
    private Integer currentMinute = 0;
}