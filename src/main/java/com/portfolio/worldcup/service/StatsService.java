package com.portfolio.worldcup.service;

import com.portfolio.worldcup.dto.ScorerDTO;
import com.portfolio.worldcup.dto.StandingDTO;
import com.portfolio.worldcup.entity.EventType;
import com.portfolio.worldcup.entity.Match;
import com.portfolio.worldcup.entity.MatchEvent;
import com.portfolio.worldcup.entity.MatchStatus;
import com.portfolio.worldcup.entity.Player;
import com.portfolio.worldcup.entity.Team;
import com.portfolio.worldcup.repository.MatchEventRepository;
import com.portfolio.worldcup.repository.MatchRepository;
import com.portfolio.worldcup.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final MatchEventRepository matchEventRepository;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    // ===================== RANKINGS POR EVENTO =====================

    public List<ScorerDTO> topScorers() {
        return rankByEventType(EventType.GOAL);
    }

    public List<ScorerDTO> topAssists() {
        return rankByEventType(EventType.ASSIST);
    }

    public List<ScorerDTO> topSaves() {
        return rankByEventType(EventType.SAVE);
    }

    /**
     * Receita generica: pega todos os eventos de um tipo, agrupa por jogador,
     * conta, ordena do maior pro menor e devolve como lista de ScorerDTO.
     */
    private List<ScorerDTO> rankByEventType(EventType type) {
        List<MatchEvent> events = matchEventRepository.findByType(type);

        // Mapa: playerId -> total de eventos. LinkedHashMap mantem ordem de insercao.
        Map<Long, Long> countByPlayer = new LinkedHashMap<>();
        // Mapa auxiliar: playerId -> o proprio Player (p/ pegar nome e time depois).
        Map<Long, Player> playerById = new LinkedHashMap<>();

        for (MatchEvent event : events) {
            Player player = event.getPlayer();
            if (player == null) {
                continue; // evento sem jogador associado: ignora
            }
            Long playerId = player.getId();
            // soma 1 no total daquele jogador
            countByPlayer.merge(playerId, 1L, Long::sum);
            playerById.put(playerId, player);
        }

        // Converte o mapa em lista de DTOs.
        List<ScorerDTO> result = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : countByPlayer.entrySet()) {
            Player player = playerById.get(entry.getKey());
            String teamName = (player.getTeam() != null) ? player.getTeam().getName() : null;
            result.add(new ScorerDTO(
                player.getId(),
                player.getName(),
                teamName,
                entry.getValue()
            ));
        }

        // Ordena do maior total pro menor.
        result.sort(Comparator.comparing(ScorerDTO::getTotal).reversed());
        return result;
    }

    // ===================== CLASSIFICACAO (STANDINGS) =====================

    public List<StandingDTO> standings() {
        List<Team> teams = teamRepository.findAll();

        // Cria uma linha (zerada) de classificacao para cada time, indexada por teamId.
        Map<Long, StandingDTO> table = new LinkedHashMap<>();
        for (Team team : teams) {
            table.put(team.getId(), new StandingDTO(
                team.getId(), team.getName(), team.getGroup(),
                0, 0, 0, 0, 0, 0, 0, 0
            ));
        }

        // So jogos FINISHED contam para a classificacao.
        List<Match> finished = matchRepository.findByStatus(MatchStatus.FINISHED);

        for (Match match : finished) {
            if (match.getHomeTeam() == null || match.getAwayTeam() == null) {
                continue;
            }
            StandingDTO home = table.get(match.getHomeTeam().getId());
            StandingDTO away = table.get(match.getAwayTeam().getId());
            if (home == null || away == null) {
                continue;
            }

            int homeGoals = match.getHomeScore() != null ? match.getHomeScore() : 0;
            int awayGoals = match.getAwayScore() != null ? match.getAwayScore() : 0;

            // jogos disputados
            home.setPlayed(home.getPlayed() + 1);
            away.setPlayed(away.getPlayed() + 1);

            // gols pro e contra
            home.setGoalsFor(home.getGoalsFor() + homeGoals);
            home.setGoalsAgainst(home.getGoalsAgainst() + awayGoals);
            away.setGoalsFor(away.getGoalsFor() + awayGoals);
            away.setGoalsAgainst(away.getGoalsAgainst() + homeGoals);

            // resultado
            if (homeGoals > awayGoals) {
                home.setWins(home.getWins() + 1);
                home.setPoints(home.getPoints() + 3);
                away.setLosses(away.getLosses() + 1);
            } else if (homeGoals < awayGoals) {
                away.setWins(away.getWins() + 1);
                away.setPoints(away.getPoints() + 3);
                home.setLosses(home.getLosses() + 1);
            } else {
                home.setDraws(home.getDraws() + 1);
                away.setDraws(away.getDraws() + 1);
                home.setPoints(home.getPoints() + 1);
                away.setPoints(away.getPoints() + 1);
            }
        }

        // saldo de gols
        List<StandingDTO> result = new ArrayList<>(table.values());
        for (StandingDTO s : result) {
            s.setGoalDifference(s.getGoalsFor() - s.getGoalsAgainst());
        }

        // ordena: pontos (desc), depois saldo (desc), depois gols pro (desc)
        result.sort(
            Comparator.comparing(StandingDTO::getPoints).reversed()
                .thenComparing(Comparator.comparing(StandingDTO::getGoalDifference).reversed())
                .thenComparing(Comparator.comparing(StandingDTO::getGoalsFor).reversed())
        );
        return result;
    }
}