package com.portfolio.worldcup.entity;

public enum EventType {
    GOAL,           // gol
    ASSIST,         // assistencia
    YELLOW_CARD,    // cartao amarelo
    RED_CARD,       // cartao vermelho
    SUBSTITUTION,   // substituicao
    PENALTY,        // penalti
    VAR,            // revisao do VAR
    SAVE            // defesa do goleiro (usado p/ ranking de goleiros)
}