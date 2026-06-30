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

    @Column(nullable = false, length = 50)
    private String provider = "API_FOOTBALL";

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

    private String league;
    private String round;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus status = MatchStatus.SCHEDULED;

    @Column(nullable = false)
    private Integer currentMinute = 0;

    // Marca se os eventos deste jogo ja foram ingeridos (evita re-buscar e gastar quota).
    @Column(nullable = false)
    private Boolean eventsIngested = false;

    // Quando os eventos deste jogo foram sincronizados pela ultima vez.
    // NULL = nunca sincronizado (maior prioridade na rotacao do scheduler).
    private LocalDateTime lastEventsSync;
}