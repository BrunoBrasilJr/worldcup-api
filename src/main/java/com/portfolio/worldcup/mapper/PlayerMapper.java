package com.portfolio.worldcup.mapper;

import com.portfolio.worldcup.dto.PlayerDTO;
import com.portfolio.worldcup.entity.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    public PlayerDTO toDTO(Player player) {
        if (player == null) {
            return null;
        }
        Long teamId = (player.getTeam() != null) ? player.getTeam().getId() : null;
        String teamName = (player.getTeam() != null) ? player.getTeam().getName() : null;
        return new PlayerDTO(
            player.getId(),
            player.getName(),
            player.getPosition(),
            player.getShirtNumber(),
            teamId,
            teamName
        );
    }
}