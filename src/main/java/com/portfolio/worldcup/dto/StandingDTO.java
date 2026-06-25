package com.portfolio.worldcup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandingDTO {
    private Long teamId;
    private String teamName;
    private String teamGroup;
    private Integer played;        // jogos disputados
    private Integer wins;
    private Integer draws;
    private Integer losses;
    private Integer goalsFor;      // gols pro
    private Integer goalsAgainst;  // gols contra
    private Integer goalDifference;// saldo de gols
    private Integer points;        // pontos (V*3 + E*1)
}