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

    // ID do time na API-Football (para casar dados na ingestao e evitar duplicar).
    @Column(unique = true)
    private Long externalId;

    @Column(nullable = false)
    private String name;          // ex: Brasil

    @Column(length = 3)
    private String code;          // ex: BRA (sigla de 3 letras)

    @Column(name = "team_group")
    private String group;         // ex: "A" (grupo na Copa)

    private String flagUrl;       // url da bandeira (opcional)

    private String logoUrl;       // url do logo do time (vem da API)
}