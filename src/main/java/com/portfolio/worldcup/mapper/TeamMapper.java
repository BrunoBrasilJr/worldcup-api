package com.portfolio.worldcup.mapper;

import com.portfolio.worldcup.dto.TeamDTO;
import com.portfolio.worldcup.entity.Team;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {

    public TeamDTO toDTO(Team team) {
        if (team == null) {
            return null;
        }
        return new TeamDTO(
            team.getId(),
            team.getName(),
            team.getCode(),
            team.getGroup(),
            team.getFlagUrl()
        );
    }
}