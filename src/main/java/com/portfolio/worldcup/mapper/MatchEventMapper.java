package com.portfolio.worldcup.mapper;

import com.portfolio.worldcup.dto.MatchEventDTO;
import com.portfolio.worldcup.entity.MatchEvent;
import org.springframework.stereotype.Component;

@Component
public class MatchEventMapper {

    public MatchEventDTO toDTO(MatchEvent event) {
        if (event == null) {
            return null;
        }
        String playerName = (event.getPlayer() != null) ? event.getPlayer().getName() : null;
        String teamName = (event.getTeam() != null) ? event.getTeam().getName() : null;
        return new MatchEventDTO(
            event.getId(),
            event.getMinute(),
            event.getType(),
            playerName,
            teamName,
            event.getDescription()
        );
    }
}