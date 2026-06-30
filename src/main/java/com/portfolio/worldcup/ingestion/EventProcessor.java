package com.portfolio.worldcup.ingestion;

import com.portfolio.worldcup.entity.*;
import com.portfolio.worldcup.ingestion.dto.ApiEventItem;
import com.portfolio.worldcup.ingestion.dto.ApiEventsResponse;
import com.portfolio.worldcup.repository.MatchEventRepository;
import com.portfolio.worldcup.repository.MatchRepository;
import com.portfolio.worldcup.repository.PlayerRepository;
import com.portfolio.worldcup.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventProcessor {

    private static final String PROVIDER = "API_FOOTBALL";

    private final MatchRepository matchRepository;
    private final MatchEventRepository matchEventRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    @Transactional
    public int processEvents(Match match, ApiEventsResponse response) {
        if (response == null || response.response == null) {
            log.warn("Resposta de eventos vazia para o jogo {}.", match.getId());
            return 0;
        }

        matchEventRepository.deleteByMatchId(match.getId());

        List<MatchEvent> toSave = new ArrayList<>();

        for (ApiEventItem item : response.response) {
            int minute = (item.time != null && item.time.elapsed != null) ? item.time.elapsed : 0;
            Team team = resolveTeam(item.team);
            Player player = resolvePlayer(item.player, team);

            EventType mainType = mapType(item.type, item.detail);
            if (mainType != null) {
                MatchEvent ev = new MatchEvent();
                ev.setMatch(match);
                ev.setTeam(team);
                ev.setPlayer(player);
                ev.setType(mainType);
                ev.setMinute(minute);
                ev.setDescription(buildDescription(item));
                toSave.add(ev);
            }

            if ("Goal".equalsIgnoreCase(item.type) && item.assist != null && item.assist.name != null) {
                Player assistant = resolvePlayer(item.assist, team);
                MatchEvent assistEv = new MatchEvent();
                assistEv.setMatch(match);
                assistEv.setTeam(team);
                assistEv.setPlayer(assistant);
                assistEv.setType(EventType.ASSIST);
                assistEv.setMinute(minute);
                assistEv.setDescription("Assistencia de " + item.assist.name);
                toSave.add(assistEv);
            }
        }

        matchEventRepository.saveAll(toSave);

        match.setEventsIngested(true);
        matchRepository.save(match);

        log.info("Eventos persistidos para o jogo {}: {} eventos.", match.getId(), toSave.size());
        return toSave.size();
    }

    private EventType mapType(String type, String detail) {
        if (type == null) {
            return null;
        }
        String t = type.toLowerCase();
        String d = detail != null ? detail.toLowerCase() : "";

        return switch (t) {
            case "goal" -> d.contains("penalty") ? EventType.PENALTY : EventType.GOAL;
            case "card" -> d.contains("red") ? EventType.RED_CARD : EventType.YELLOW_CARD;
            case "subst" -> EventType.SUBSTITUTION;
            case "var" -> EventType.VAR;
            default -> null;
        };
    }

    private String buildDescription(ApiEventItem item) {
        StringBuilder sb = new StringBuilder();
        if (item.detail != null) sb.append(item.detail);
        if (item.player != null && item.player.name != null) {
            sb.append(" - ").append(item.player.name);
        }
        if (item.comments != null) sb.append(" (").append(item.comments).append(")");
        return sb.length() > 0 ? sb.toString() : null;
    }

    private Team resolveTeam(ApiEventItem.TeamRef ref) {
        if (ref == null || ref.id == null) {
            return null;
        }
        return teamRepository.findByProviderAndExternalId(PROVIDER, ref.id)
                .orElseGet(() -> {
                    Team t = new Team();
                    t.setProvider(PROVIDER);
                    t.setExternalId(ref.id);
                    t.setName(ref.name);
                    return teamRepository.save(t);
                });
    }

    private Player resolvePlayer(ApiEventItem.PlayerRef ref, Team team) {
        if (ref == null || ref.name == null) {
            return null;
        }
        Player player = playerRepository.findFirstByName(ref.name)
                .orElseGet(() -> {
                    Player p = new Player();
                    p.setName(ref.name);
                    return p;
                });

        if (player.getTeam() == null && team != null) {
            player.setTeam(team);
        }

        return playerRepository.save(player);
    }
}