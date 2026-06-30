package com.portfolio.worldcup.ingestion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Espelha a resposta inteira de /fixtures/events.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiEventsResponse {

    public Integer results;
    public List<ApiEventItem> response;
}