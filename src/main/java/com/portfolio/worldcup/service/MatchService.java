package com.portfolio.worldcup.service;

import com.portfolio.worldcup.entity.Match;
import com.portfolio.worldcup.entity.MatchEvent;
import com.portfolio.worldcup.entity.MatchStatus;
import com.portfolio.worldcup.exception.ResourceNotFoundException;
import com.portfolio.worldcup.repository.MatchEventRepository;
import com.portfolio.worldcup.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final MatchEventRepository matchEventRepository;

    public List<Match> findToday() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime startOfTomorrow = LocalDate.now().plusDays(1).atStartOfDay();
        return matchRepository.findByMatchDateTimeBetween(startOfDay, startOfTomorrow);
    }

    public List<Match> findLive() {
        List<MatchStatus> liveStatuses = List.of(
            MatchStatus.LIVE,
            MatchStatus.HALFTIME,
            MatchStatus.EXTRA_TIME,
            MatchStatus.PENALTIES
        );
        return matchRepository.findByStatusIn(liveStatuses);
    }

    public List<Match> findAll() {
        return matchRepository.findAll();
    }

    // Agora LANCA excecao 404 em vez de retornar null.
    public Match findById(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jogo", id));
    }

    public List<MatchEvent> findEventsByMatch(Long matchId) {
        return matchEventRepository.findByMatchIdOrderByMinuteAsc(matchId);
    }
}