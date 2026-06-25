package com.portfolio.worldcup.entity;

public enum MatchStatus {
    SCHEDULED,   // jogo agendado, ainda nao comecou
    LIVE,        // jogo rolando (1o ou 2o tempo)
    HALFTIME,    // intervalo
    EXTRA_TIME,  // prorrogacao
    PENALTIES,   // disputa de penaltis
    FINISHED     // jogo encerrado
}