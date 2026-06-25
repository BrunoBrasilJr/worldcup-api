package com.portfolio.worldcup.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Atalho util: monta a mensagem padrao "X com id Y nao encontrado".
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " com id " + id + " nao encontrado(a).");
    }
}