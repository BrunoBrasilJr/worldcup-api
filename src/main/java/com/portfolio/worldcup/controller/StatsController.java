package com.portfolio.worldcup.controller;

import com.portfolio.worldcup.dto.ScorerDTO;
import com.portfolio.worldcup.dto.StandingDTO;
import com.portfolio.worldcup.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/stats/top-scorers")
    public List<ScorerDTO> topScorers() {
        return statsService.topScorers();
    }

    @GetMapping("/stats/top-assists")
    public List<ScorerDTO> topAssists() {
        return statsService.topAssists();
    }

    @GetMapping("/stats/top-saves")
    public List<ScorerDTO> topSaves() {
        return statsService.topSaves();
    }

    @GetMapping("/standings")
    public List<StandingDTO> standings() {
        return statsService.standings();
    }
}