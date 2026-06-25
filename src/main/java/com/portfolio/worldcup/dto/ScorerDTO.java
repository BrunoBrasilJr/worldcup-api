package com.portfolio.worldcup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScorerDTO {
    private Long playerId;
    private String playerName;
    private String teamName;
    private Long total;        // qtd de gols / assistencias / defesas
}