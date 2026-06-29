package com.portfolio.worldcup.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teams")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Fonte de dados (ex: API_FOOTBALL). Parte da identidade externa.
    @Column(nullable = false, length = 50)
    private String provider = "API_FOOTBALL";

    // ID do time no provider (para casar dados na ingestao e evitar duplicar).
    private Long externalId;

    @Column(nullable = false)
    private String name;

    @Column(length = 3)
    private String code;

    @Column(name = "team_group")
    private String group;

    private String flagUrl;

    private String logoUrl;
}