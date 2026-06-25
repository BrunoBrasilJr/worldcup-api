package com.portfolio.worldcup.dto;

import com.portfolio.worldcup.entity.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchEventDTO {
    private Long id;
    private Integer minute;
    private EventType type;
    private String playerName;
    private String teamName;
    private String description;
}