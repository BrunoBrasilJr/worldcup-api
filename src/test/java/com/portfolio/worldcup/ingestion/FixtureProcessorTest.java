package com.portfolio.worldcup.ingestion;

import com.portfolio.worldcup.entity.Match;
import com.portfolio.worldcup.entity.MatchStatus;
import com.portfolio.worldcup.entity.Team;
import com.portfolio.worldcup.ingestion.dto.ApiFixtureItem;
import com.portfolio.worldcup.repository.MatchRepository;
import com.portfolio.worldcup.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Testes de regra de negocio do FixtureProcessor.
 * Unitarios puros: os repositories sao mockados (sem subir o Spring nem o banco).
 */
@ExtendWith(MockitoExtension.class)
class FixtureProcessorTest {

    // Dependencias mockadas: nao tocam o banco real.
    @Mock
    private TeamRepository teamRepository;

    @Mock
    private MatchRepository matchRepository;

    // Classe sob teste: o Mockito injeta os mocks acima nela.
    @InjectMocks
    private FixtureProcessor fixtureProcessor;

    /**
     * Helper: monta um ApiFixtureItem minimo para os testes,
     * com ids de time, status e placar configuraveis.
     */
    private ApiFixtureItem buildFixture(Long fixtureId, Long homeId, Long awayId,
                                        String statusShort, int homeGoals, int awayGoals) {
        ApiFixtureItem item = new ApiFixtureItem();

        item.fixture = new ApiFixtureItem.Fixture();
        item.fixture.id = fixtureId;
        item.fixture.status = new ApiFixtureItem.Status();
        item.fixture.status.shortName = statusShort;
        item.fixture.status.elapsed = 45;

        item.teams = new ApiFixtureItem.Teams();
        item.teams.home = new ApiFixtureItem.TeamInfo();
        item.teams.home.id = homeId;
        item.teams.home.name = "Home FC";
        item.teams.away = new ApiFixtureItem.TeamInfo();
        item.teams.away.id = awayId;
        item.teams.away.name = "Away FC";

        item.goals = new ApiFixtureItem.Goals();
        item.goals.home = homeGoals;
        item.goals.away = awayGoals;

        return item;
    }

    @Test
    void process_deveCriarTimesQuandoNaoExistem_ePersistirOJogo() {
        // ===== Arrange =====
        // Cenario: jogo novo, times ainda nao existem no banco.
        ApiFixtureItem item = buildFixture(1001L, 10L, 20L, "1H", 1, 0);

        // Times nao existem -> repository retorna vazio, forcando criacao.
        when(teamRepository.findByProviderAndExternalId(eq("API_FOOTBALL"), anyLong()))
                .thenReturn(Optional.empty());
        // Ao salvar um time, devolve o proprio time (simula o save).
        when(teamRepository.save(any(Team.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        // Jogo nao existe -> forca criacao de um Match novo.
        when(matchRepository.findByProviderAndExternalId(eq("API_FOOTBALL"), eq(1001L)))
                .thenReturn(Optional.empty());

        // ===== Act =====
        fixtureProcessor.process(item);

        // ===== Assert =====
        // Captura o Match efetivamente salvo para inspecionar seus campos.
        ArgumentCaptor<Match> matchCaptor = ArgumentCaptor.forClass(Match.class);
        // O save do match e chamado uma vez; capturamos o argumento.
        org.mockito.Mockito.verify(matchRepository).save(matchCaptor.capture());
        Match saved = matchCaptor.getValue();

        // Regra: status "1H" da API deve mapear para LIVE no dominio.
        assertThat(saved.getStatus()).isEqualTo(MatchStatus.LIVE);
        // Regra: placar e externalId persistidos corretamente.
        assertThat(saved.getHomeScore()).isEqualTo(1);
        assertThat(saved.getAwayScore()).isEqualTo(0);
        assertThat(saved.getExternalId()).isEqualTo(1001L);
        assertThat(saved.getProvider()).isEqualTo("API_FOOTBALL");
        // Regra: os times foram resolvidos (criados) e associados.
        assertThat(saved.getHomeTeam()).isNotNull();
        assertThat(saved.getAwayTeam()).isNotNull();
    }

    @Test
    void process_deveMapearStatusFinalizado_paraFINISHED() {
        // ===== Arrange =====
        // Cenario: jogo com status "FT" (full time) na API.
        ApiFixtureItem item = buildFixture(1002L, 10L, 20L, "FT", 2, 1);

        when(teamRepository.findByProviderAndExternalId(eq("API_FOOTBALL"), anyLong()))
                .thenReturn(Optional.empty());
        when(teamRepository.save(any(Team.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(matchRepository.findByProviderAndExternalId(eq("API_FOOTBALL"), eq(1002L)))
                .thenReturn(Optional.empty());

        // ===== Act =====
        fixtureProcessor.process(item);

        // ===== Assert =====
        ArgumentCaptor<Match> matchCaptor = ArgumentCaptor.forClass(Match.class);
        org.mockito.Mockito.verify(matchRepository).save(matchCaptor.capture());
        Match saved = matchCaptor.getValue();

        // Regra: "FT" deve mapear para FINISHED.
        assertThat(saved.getStatus()).isEqualTo(MatchStatus.FINISHED);
        assertThat(saved.getHomeScore()).isEqualTo(2);
        assertThat(saved.getAwayScore()).isEqualTo(1);
    }
}