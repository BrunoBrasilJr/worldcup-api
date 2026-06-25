package com.portfolio.worldcup.dto;

import com.portfolio.worldcup.entity.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDTO {
    private Long id;
    private String homeTeamName;
    private String awayTeamName;
    private Integer homeScore;
    private Integer awayScore;
    private LocalDateTime matchDateTime;
    private String stadium;
    private String city;
    private MatchStatus status;
    private Integer currentMinute;
}