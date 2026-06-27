package com.portfolio.worldcup.ingestion;

import com.portfolio.worldcup.entity.Match;
import com.portfolio.worldcup.entity.MatchStatus;
import com.portfolio.worldcup.entity.Team;
import com.portfolio.worldcup.ingestion.dto.ApiFixtureItem;
import com.portfolio.worldcup.ingestion.dto.ApiFixturesResponse;
import com.portfolio.worldcup.repository.MatchRepository;
import com.portfolio.worldcup.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class IngestionService {

    private final ApiFootballClient apiFootballClient;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    @Transactional
    public int ingestLiveFixtures() {
        // O client ja devolve o DTO pronto (sem ObjectMapper manual).
        ApiFixturesResponse parsed = apiFootballClient.getLiveFixtures();

        if (parsed == null || parsed.response == null || parsed.response.isEmpty()) {
            System.out.println(">>> Nenhum jogo ao vivo no momento.");
            return 0;
        }

        int count = 0;
        for (ApiFixtureItem item : parsed.response) {
            try {
                processFixture(item);
                count++;
            } catch (Exception e) {
                System.out.println(">>> Erro ao processar jogo: " + e.getMessage());
            }
        }

        System.out.println(">>> Ingestao concluida: " + count + " jogos processados.");
        return count;
    }

    private void processFixture(ApiFixtureItem item) {
        if (item.fixture == null || item.teams == null) {
            return;
        }

        Team home = resolveTeam(item.teams.home);
        Team away = resolveTeam(item.teams.away);

        Long externalId = item.fixture.id;

        Match match = matchRepository.findByExternalId(externalId).orElseGet(Match::new);
        match.setExternalId(externalId);
        match.setHomeTeam(home);
        match.setAwayTeam(away);

        if (item.goals != null) {
            match.setHomeScore(item.goals.home != null ? item.goals.home : 0);
            match.setAwayScore(item.goals.away != null ? item.goals.away : 0);
        }

        if (item.fixture.venue != null) {
            match.setStadium(item.fixture.venue.name);
            match.setCity(item.fixture.venue.city);
        }

        if (item.league != null) {
            match.setLeague(item.league.name);
            match.setRound(item.league.round);
        }

        if (item.fixture.date != null) {
            try {
                match.setMatchDateTime(
                    OffsetDateTime.parse(item.fixture.date).toLocalDateTime()
                );
            } catch (Exception ignored) {
            }
        }

        if (item.fixture.status != null) {
            match.setStatus(mapStatus(item.fixture.status.shortName));
            match.setCurrentMinute(
                item.fixture.status.elapsed != null ? item.fixture.status.elapsed : 0
            );
        }

        matchRepository.save(match);
    }

    private Team resolveTeam(ApiFixtureItem.TeamInfo info) {
        if (info == null || info.id == null) {
            return null;
        }
        return teamRepository.findByExternalId(info.id).orElseGet(() -> {
            Team t = new Team();
            t.setExternalId(info.id);
            t.setName(info.name);
            t.setLogoUrl(info.logo);
            return teamRepository.save(t);
        });
    }

    private MatchStatus mapStatus(String shortStatus) {
        if (shortStatus == null) {
            return MatchStatus.SCHEDULED;
        }
        return switch (shortStatus) {
            case "1H", "2H", "LIVE" -> MatchStatus.LIVE;
            case "HT"               -> MatchStatus.HALFTIME;
            case "ET", "BT"         -> MatchStatus.EXTRA_TIME;
            case "P", "PEN"         -> MatchStatus.PENALTIES;
            case "FT", "AET"        -> MatchStatus.FINISHED;
            default                  -> MatchStatus.SCHEDULED;
        };
    }
}