-- ============================================================
-- V3 - Indices para otimizar as consultas mais frequentes.
-- Indices nao alteram resultados, apenas a velocidade das buscas.
-- ============================================================

-- ---------- MATCHES ----------
-- Busca por status (jogos ao vivo, agendados, encerrados).
CREATE INDEX idx_matches_status ON matches (status);

-- Busca por data/hora (jogos de hoje, intervalos de data).
CREATE INDEX idx_matches_date ON matches (match_date_time);

-- Busca por external_id na ingestao.
CREATE INDEX idx_matches_external_id ON matches (external_id);

-- Foreign keys (usadas em joins ao montar os DTOs).
CREATE INDEX idx_matches_home_team ON matches (home_team_id);
CREATE INDEX idx_matches_away_team ON matches (away_team_id);

-- ---------- TEAMS ----------
CREATE INDEX idx_teams_external_id ON teams (external_id);

-- ---------- MATCH_EVENTS ----------
-- Busca de eventos por jogo (timeline) e por tipo (estatisticas).
CREATE INDEX idx_events_match ON match_events (match_id);
CREATE INDEX idx_events_type  ON match_events (type);

-- Foreign keys.
CREATE INDEX idx_events_player ON match_events (player_id);
CREATE INDEX idx_events_team   ON match_events (team_id);

-- ---------- PLAYERS ----------
CREATE INDEX idx_players_team ON players (team_id);