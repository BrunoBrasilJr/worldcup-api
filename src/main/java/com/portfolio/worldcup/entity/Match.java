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

    // ----- Time mandante (casa) -----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;

    // ----- Time visitante (fora) -----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;

    // ----- Placar -----
    @Column(nullable = false)
    private Integer homeScore = 0;

    @Column(nullable = false)
    private Integer awayScore = 0;

    // ----- Data/hora e local -----
    private LocalDateTime matchDateTime;   // quando o jogo acontece
    private String stadium;                // ex: Maracana
    private String city;                   // ex: Rio de Janeiro

    // ----- Status do jogo (enum) -----
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus status = MatchStatus.SCHEDULED;

    // ----- Minuto atual do jogo (usado no tempo real) -----
    @Column(nullable = false)
    private Integer currentMinute = 0;
}