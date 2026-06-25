package com.portfolio.worldcup.bootstrap;

import com.portfolio.worldcup.entity.*;
import com.portfolio.worldcup.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final MatchEventRepository matchEventRepository;

    @Override
    public void run(String... args) {

        if (teamRepository.count() > 0) {
            return;
        }

        // ---------- TIMES ----------
        Team brasil    = new Team(null, "Brasil",    "BRA", "A", null);
        Team argentina = new Team(null, "Argentina", "ARG", "A", null);
        Team franca    = new Team(null, "Franca",    "FRA", "B", null);
        Team espanha   = new Team(null, "Espanha",   "ESP", "B", null);
        teamRepository.saveAll(List.of(brasil, argentina, franca, espanha));

        // ---------- JOGADORES (guardo referencias p/ usar nos eventos) ----------
        Player neymar   = new Player(null, "Neymar",   "FORWARD",    10, brasil);
        Player vinicius = new Player(null, "Vinicius", "FORWARD",     7, brasil);
        Player casemiro = new Player(null, "Casemiro", "MIDFIELDER",  5, brasil);
        Player alisson  = new Player(null, "Alisson",  "GOALKEEPER",  1, brasil);

        Player messi    = new Player(null, "Messi",    "FORWARD",    10, argentina);
        Player alvarez  = new Player(null, "Alvarez",  "FORWARD",     9, argentina);
        Player dePaul   = new Player(null, "De Paul",  "MIDFIELDER",  7, argentina);
        Player martinez = new Player(null, "Martinez", "GOALKEEPER", 23, argentina);

        Player mbappe    = new Player(null, "Mbappe",    "FORWARD",    10, franca);
        Player griezmann = new Player(null, "Griezmann", "MIDFIELDER",  7, franca);
        Player maignan   = new Player(null, "Maignan",   "GOALKEEPER", 16, franca);

        Player yamal = new Player(null, "Yamal", "FORWARD",    19, espanha);
        Player pedri = new Player(null, "Pedri", "MIDFIELDER",  8, espanha);
        Player simon = new Player(null, "Simon", "GOALKEEPER", 23, espanha);

        playerRepository.saveAll(List.of(
            neymar, vinicius, casemiro, alisson,
            messi, alvarez, dePaul, martinez,
            mbappe, griezmann, maignan,
            yamal, pedri, simon
        ));

        // ---------- JOGOS ----------
        LocalDateTime agora = LocalDateTime.now();

        // Jogo 1: terminou (ontem) -> Brasil 2 x 1 Argentina
        Match jogo1 = new Match();
        jogo1.setHomeTeam(brasil);
        jogo1.setAwayTeam(argentina);
        jogo1.setHomeScore(2);
        jogo1.setAwayScore(1);
        jogo1.setMatchDateTime(agora.minusDays(1));
        jogo1.setStadium("Maracana");
        jogo1.setCity("Rio de Janeiro");
        jogo1.setStatus(MatchStatus.FINISHED);
        jogo1.setCurrentMinute(90);

        // Jogo 2: HOJE e AO VIVO -> Franca 1 x 0 Espanha, 30 min
        Match jogo2 = new Match();
        jogo2.setHomeTeam(franca);
        jogo2.setAwayTeam(espanha);
        jogo2.setHomeScore(1);
        jogo2.setAwayScore(0);
        jogo2.setMatchDateTime(agora);
        jogo2.setStadium("Lusail");
        jogo2.setCity("Doha");
        jogo2.setStatus(MatchStatus.LIVE);
        jogo2.setCurrentMinute(30);

        // Jogo 3: agendado p/ HOJE mais tarde -> Brasil x Franca
        Match jogo3 = new Match();
        jogo3.setHomeTeam(brasil);
        jogo3.setAwayTeam(franca);
        jogo3.setMatchDateTime(agora.plusHours(3));
        jogo3.setStadium("Allianz Parque");
        jogo3.setCity("Sao Paulo");
        jogo3.setStatus(MatchStatus.SCHEDULED);

        matchRepository.saveAll(List.of(jogo1, jogo2, jogo3));

        // ---------- EVENTOS DO JOGO 1 (Brasil 2 x 1 Argentina) ----------
        matchEventRepository.saveAll(List.of(
            // 1o gol do Brasil: Neymar (assist de Vinicius)
            new MatchEvent(null, jogo1, neymar,   brasil,    EventType.GOAL,   23, "Gol de Neymar"),
            new MatchEvent(null, jogo1, vinicius, brasil,    EventType.ASSIST, 23, "Assistencia de Vinicius"),
            // 2o gol do Brasil: Vinicius (assist de Neymar)
            new MatchEvent(null, jogo1, vinicius, brasil,    EventType.GOAL,   58, "Gol de Vinicius"),
            new MatchEvent(null, jogo1, neymar,   brasil,    EventType.ASSIST, 58, "Assistencia de Neymar"),
            // Gol da Argentina: Messi
            new MatchEvent(null, jogo1, messi,    argentina, EventType.GOAL,   71, "Gol de Messi"),
            new MatchEvent(null, jogo1, alvarez,  argentina, EventType.ASSIST, 71, "Assistencia de Alvarez"),
            // Cartao
            new MatchEvent(null, jogo1, casemiro, brasil,    EventType.YELLOW_CARD, 65, "Cartao amarelo p/ Casemiro"),
            // Defesas (SAVE) dos goleiros -> alimenta o ranking de goleiros
            new MatchEvent(null, jogo1, alisson,  brasil,    EventType.SAVE, 40, "Defesa de Alisson"),
            new MatchEvent(null, jogo1, alisson,  brasil,    EventType.SAVE, 75, "Defesa de Alisson"),
            new MatchEvent(null, jogo1, alisson,  brasil,    EventType.SAVE, 88, "Defesa de Alisson"),
            new MatchEvent(null, jogo1, martinez, argentina, EventType.SAVE, 50, "Defesa de Martinez"),
            new MatchEvent(null, jogo1, martinez, argentina, EventType.SAVE, 80, "Defesa de Martinez")
        ));

        // ---------- EVENTO DO JOGO 2 (Franca 1 x 0 Espanha, ao vivo) ----------
        matchEventRepository.saveAll(List.of(
            new MatchEvent(null, jogo2, mbappe, franca,  EventType.GOAL, 12, "Gol de Mbappe"),
            new MatchEvent(null, jogo2, simon,  espanha, EventType.SAVE, 20, "Defesa de Simon")
        ));

        System.out.println(">>> DataSeeder: banco populado com times, jogadores, jogos e eventos!");
    }
}