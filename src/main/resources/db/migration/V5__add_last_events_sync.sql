-- ============================================================
-- V5 - Campo para rotacao justa na sincronizacao de eventos.
-- Guarda QUANDO os eventos de cada jogo foram sincronizados pela ultima vez.
-- NULL = nunca sincronizado (maior prioridade na rotacao).
-- ============================================================

ALTER TABLE matches ADD COLUMN last_events_sync TIMESTAMP;