package com.portfolio.worldcup.ingestion.exception;

/**
 * Erro TRANSITORIO na chamada a API externa (timeout, conexao, 5xx,
 * ou 429 com Retry-After). Deve disparar retry com backoff.
 */
public class TransientApiException extends RuntimeException {

    public TransientApiException(String message) {
        super(message);
    }

    public TransientApiException(String message, Throwable cause) {
        super(message, cause);
    }
}