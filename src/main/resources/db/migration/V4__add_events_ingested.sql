-- ============================================================
-- V4 - Flag para controlar a ingestao de eventos por partida.
-- Evita re-buscar eventos de jogos ja processados (economia de quota).
-- ============================================================

ALTER TABLE matches ADD COLUMN events_ingested BOOLEAN NOT NULL DEFAULT FALSE;