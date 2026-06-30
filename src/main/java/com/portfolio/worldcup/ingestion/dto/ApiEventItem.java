package com.portfolio.worldcup.ingestion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Espelha UM evento do array "response" de /fixtures/events.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiEventItem {

    public Time time;
    public TeamRef team;
    public PlayerRef player;
    public PlayerRef assist;
    public String type;      // "Goal", "Card", "subst", "Var"
    public String detail;    // "Normal Goal", "Penalty", "Yellow Card", "Red Card"...
    public String comments;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Time {
        public Integer elapsed;       // minuto
        public Integer extra;         // acrescimos
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TeamRef {
        public Long id;
        public String name;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlayerRef {
        public Long id;
        public String name;
    }
}