package com.portfolio.worldcup.ingestion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Espelha a resposta inteira do /fixtures.
 * O que nos interessa e o array "response" (lista de jogos).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiFixturesResponse {

    public Integer results;                 // quantos jogos vieram
    public List<ApiFixtureItem> response;   // a lista de jogos
}