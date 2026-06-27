package com.portfolio.worldcup.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;

// DESATIVADO: este seeder criava dados ficticios (Brasil, Argentina...).
// Agora os dados vem da ingestao real da API-Football, entao ele nao roda mais.
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    @Override
    public void run(String... args) {
        // Sem dados ficticios. A ingestao real cuida de popular o banco.
    }
}