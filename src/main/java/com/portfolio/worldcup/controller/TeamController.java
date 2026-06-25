package com.portfolio.worldcup.controller;

import com.portfolio.worldcup.dto.TeamDTO;
import com.portfolio.worldcup.mapper.TeamMapper;
import com.portfolio.worldcup.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final TeamMapper teamMapper;

    @GetMapping("/teams")
    public List<TeamDTO> getAll() {
        return teamService.findAll()
                .stream()
                .map(teamMapper::toDTO)
                .toList();
    }
}