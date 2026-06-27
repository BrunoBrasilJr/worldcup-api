package com.portfolio.worldcup.ingestion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiFixtureItem {

    public Fixture fixture;
    public League league;
    public Teams teams;
    public Goals goals;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Fixture {
        public Long id;
        public String date;
        public Venue venue;
        public Status status;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Venue {
        public String name;
        public String city;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Status {
        @JsonProperty("long")
        public String longName;    // JSON manda "long"
        @JsonProperty("short")
        public String shortName;   // JSON manda "short"
        public Integer elapsed;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class League {
        public Long id;
        public String name;
        public String round;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Teams {
        public TeamInfo home;
        public TeamInfo away;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TeamInfo {
        public Long id;
        public String name;
        public String logo;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Goals {
        public Integer home;
        public Integer away;
    }
}