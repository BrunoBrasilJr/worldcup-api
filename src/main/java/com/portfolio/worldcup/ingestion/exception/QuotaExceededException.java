package com.portfolio.worldcup.ingestion.exception;

/**
 * Quota da API esgotada (HTTP 429 SEM cabecalho Retry-After).
 * NAO deve disparar retry: nao adianta insistir, a quota nao volta agora.
 * Deixamos o proximo ciclo do scheduler tentar novamente.
 */
public class QuotaExceededException extends RuntimeException {

    public QuotaExceededException(String message) {
        super(message);
    }
}