package com.portfolio.worldcup.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "players")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;          // ex: Neymar

    private String position;      // ex: FORWARD, GOALKEEPER, MIDFIELDER, DEFENDER

    private Integer shirtNumber;  // ex: 10

    // ----- Relacionamento: muitos jogadores pertencem a UM time -----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
}