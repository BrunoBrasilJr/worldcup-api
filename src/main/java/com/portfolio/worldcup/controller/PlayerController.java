package com.portfolio.worldcup.controller;

import com.portfolio.worldcup.dto.PlayerDTO;
import com.portfolio.worldcup.mapper.PlayerMapper;
import com.portfolio.worldcup.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerMapper playerMapper;

    @GetMapping("/players")
    public List<PlayerDTO> getAll() {
        return playerService.findAll()
                .stream()
                .map(playerMapper::toDTO)
                .toList();
    }
}