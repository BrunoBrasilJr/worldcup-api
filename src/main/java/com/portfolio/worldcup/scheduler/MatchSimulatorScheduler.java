package com.portfolio.worldcup.scheduler;

import com.portfolio.worldcup.entity.*;
import com.portfolio.worldcup.repository.MatchEventRepository;
import com.portfolio.worldcup.repository.MatchRepository;
import com.portfolio.worldcup.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class MatchSimulatorScheduler {

    private final MatchRepository matchRepository;
    private final MatchEventRepository matchEventRepository;
    private final PlayerRepository playerRepository;

    private final Random random = new Random();

    /**
     * Roda a cada 2 segundos. Cada execucao = 1 "minuto" de jogo.
     * fixedRate = 2000 (milissegundos).
     */
    @Scheduled(fixedRate = 2000)
    @Transactional
    public void simulate() {

        // Pega todos os jogos que estao acontecendo (LIVE ou HALFTIME).
        List<Match> activeMatches = matchRepository.findByStatusIn(
            List.of(MatchStatus.LIVE, MatchStatus.HALFTIME)
        );

        for (Match match : activeMatches) {

            // ---------- INTERVALO (HALFTIME) ----------
            if (match.getStatus() == MatchStatus.HALFTIME) {
                // Usamos o currentMinute como contador do intervalo.
                // Ele fica "preso" em 45; quando passar 3 tiques, volta a jogar.
                // Reaproveitamos currentMinute somando ate 48 e ai voltamos pra LIVE no 46.
                int halftimeCounter = match.getCurrentMinute();
                if (halftimeCounter >= 48) {
                    match.setStatus(MatchStatus.LIVE);
                    match.setCurrentMinute(46);   // comeca o 2o tempo
                    System.out.println(">>> [" + nome(match) + "] Comeca o 2o tempo!");
                } else {
                    match.setCurrentMinute(halftimeCounter + 1); // ainda no intervalo
                }
                matchRepository.save(match);
                continue; // proximo jogo
            }

            // ---------- JOGO ROLANDO (LIVE) ----------
            int minute = match.getCurrentMinute() + 1;
            match.setCurrentMinute(minute);

            // Fim do 1o tempo -> intervalo
            if (minute == 45) {
                match.setStatus(MatchStatus.HALFTIME);
                System.out.println(">>> [" + nome(match) + "] Fim do 1o tempo (intervalo).");
                matchRepository.save(match);
                continue;
            }

            // Fim do jogo
            if (minute >= 90) {
                match.setCurrentMinute(90);
                match.setStatus(MatchStatus.FINISHED);
                System.out.println(">>> [" + nome(match) + "] Fim de jogo! Placar final: "
                        + match.getHomeScore() + " x " + match.getAwayScore());
                matchRepository.save(match);
                continue;
            }

            // ---------- GOL ALEATORIO (10% de chance por minuto) ----------
            if (random.nextInt(100) < 10) {
                marcarGol(match, minute);
            }

            matchRepository.save(match);
        }
    }

    /**
     * Sorteia um dos dois times, sorteia um jogador desse time,
     * incrementa o placar e cria um evento GOAL.
     */
    private void marcarGol(Match match, int minute) {
        // Sorteia true = time da casa, false = visitante
        boolean homeScored = random.nextBoolean();
        Team scoringTeam = homeScored ? match.getHomeTeam() : match.getAwayTeam();
        if (scoringTeam == null) {
            return;
        }

        // Atualiza o placar
        if (homeScored) {
            match.setHomeScore(match.getHomeScore() + 1);
        } else {
            match.setAwayScore(match.getAwayScore() + 1);
        }

        // Sorteia um jogador do time que marcou
        List<Player> squad = playerRepository.findByTeamId(scoringTeam.getId());
        Player scorer = squad.isEmpty() ? null : squad.get(random.nextInt(squad.size()));

        // Cria o evento de gol
        MatchEvent goal = new MatchEvent(
            null, match, scorer, scoringTeam,
            EventType.GOAL, minute,
            "Gol simulado" + (scorer != null ? " de " + scorer.getName() : "")
        );
        matchEventRepository.save(goal);

        System.out.println(">>> GOL! [" + nome(match) + "] min " + minute
                + " - " + (scorer != null ? scorer.getName() : "?")
                + " (" + scoringTeam.getName() + ") | Placar: "
                + match.getHomeScore() + " x " + match.getAwayScore());
    }

    private String nome(Match match) {
        String h = match.getHomeTeam() != null ? match.getHomeTeam().getName() : "?";
        String a = match.getAwayTeam() != null ? match.getAwayTeam().getName() : "?";
        return h + " x " + a;
    }
}