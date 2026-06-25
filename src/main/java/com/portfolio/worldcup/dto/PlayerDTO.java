package com.portfolio.worldcup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDTO {
    private Long id;
    private String name;
    private String position;
    private Integer shirtNumber;
    private Long teamId;
    private String teamName;   // achatamos o time em 2 campos simples, sem aninhar o objeto
}