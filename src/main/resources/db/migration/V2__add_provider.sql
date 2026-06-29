-- ============================================================
-- V2 - Adiciona o conceito de "provider" (fonte de dados).
-- Permite multiplos provedores no futuro sem colisao de external_id.
-- A identidade passa a ser composta: (provider, external_id).
-- ============================================================

-- ---------- TEAMS ----------
-- 1) Adiciona a coluna provider com default para os registros existentes.
ALTER TABLE teams ADD COLUMN provider VARCHAR(50) NOT NULL DEFAULT 'API_FOOTBALL';

-- 2) Remove a constraint unica antiga (so external_id).
ALTER TABLE teams DROP CONSTRAINT uk_teams_external_id;

-- 3) Cria a nova constraint unica composta (provider + external_id).
ALTER TABLE teams ADD CONSTRAINT uk_teams_provider_external_id UNIQUE (provider, external_id);

-- ---------- MATCHES ----------
ALTER TABLE matches ADD COLUMN provider VARCHAR(50) NOT NULL DEFAULT 'API_FOOTBALL';
ALTER TABLE matches DROP CONSTRAINT uk_matches_external_id;
ALTER TABLE matches ADD CONSTRAINT uk_matches_provider_external_id UNIQUE (provider, external_id);