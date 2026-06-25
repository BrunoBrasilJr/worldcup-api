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

    @Column(nullable = false)
    private String name;          // ex: Brasil

    @Column(length = 3)
    private String code;          // ex: BRA (sigla de 3 letras)

    @Column(name = "team_group")
    private String group;         // ex: "A" (grupo na Copa) -> coluna renomeada p/ evitar palavra reservada SQL

    private String flagUrl;       // url da bandeira (opcional)
}