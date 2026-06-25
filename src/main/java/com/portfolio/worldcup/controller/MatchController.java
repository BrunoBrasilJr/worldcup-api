package com.portfolio.worldcup.controller;

import com.portfolio.worldcup.dto.MatchDTO;
import com.portfolio.worldcup.dto.MatchEventDTO;
import com.portfolio.worldcup.mapper.MatchEventMapper;
import com.portfolio.worldcup.mapper.MatchMapper;
import com.portfolio.worldcup.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final MatchMapper matchMapper;
    private final MatchEventMapper matchEventMapper;

    // GET /matches/today
    @GetMapping("/today")
    public List<MatchDTO> today() {
        return matchService.findToday()
                .stream()
                .map(matchMapper::toDTO)
                .toList();
    }

    // GET /matches/live
    @GetMapping("/live")
    public List<MatchDTO> live() {
        return matchService.findLive()
                .stream()
                .map(matchMapper::toDTO)
                .toList();
    }

    // GET /matches/{id}
    @GetMapping("/{id}")
    public MatchDTO byId(@PathVariable Long id) {
        return matchMapper.toDTO(matchService.findById(id));
    }

    // GET /matches/{id}/events
    @GetMapping("/{id}/events")
    public List<MatchEventDTO> events(@PathVariable Long id) {
        return matchService.findEventsByMatch(id)
                .stream()
                .map(matchEventMapper::toDTO)
                .toList();
    }
}