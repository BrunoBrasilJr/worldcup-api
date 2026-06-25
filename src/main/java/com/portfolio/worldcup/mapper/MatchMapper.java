package com.portfolio.worldcup.mapper;

import com.portfolio.worldcup.dto.MatchDTO;
import com.portfolio.worldcup.entity.Match;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper {

    public MatchDTO toDTO(Match match) {
        if (match == null) {
            return null;
        }
        String homeName = (match.getHomeTeam() != null) ? match.getHomeTeam().getName() : null;
        String awayName = (match.getAwayTeam() != null) ? match.getAwayTeam().getName() : null;
        return new MatchDTO(
            match.getId(),
            homeName,
            awayName,
            match.getHomeScore(),
            match.getAwayScore(),
            match.getMatchDateTime(),
            match.getStadium(),
            match.getCity(),
            match.getStatus(),
            match.getCurrentMinute()
        );
    }
}