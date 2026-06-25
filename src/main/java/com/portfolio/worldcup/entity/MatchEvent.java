package com.portfolio.worldcup.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "match_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ----- A qual jogo este evento pertence -----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    // ----- Qual jogador realizou o evento -----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    // ----- Time do evento -----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    // ----- Tipo do evento (enum) -----
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType type;

    // ----- Minuto em que ocorreu (coluna renomeada: 'minute' e palavra reservada no SQL) -----
    @Column(name = "event_minute", nullable = false)
    private Integer minute;

    // ----- Descricao livre -----
    private String description;   // ex: "Golaco de fora da area"
}