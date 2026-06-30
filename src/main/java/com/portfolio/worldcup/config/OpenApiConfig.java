package com.portfolio.worldcup.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    /**
     * Metadados da API exibidos no cabecalho do Swagger UI e na spec OpenAPI.
     */
    @Bean
    public OpenAPI worldCupOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("World Cup API")
                        .description("API REST para dados da Copa do Mundo 2026: jogos, "
                                + "times, jogadores, eventos (gols, cartoes, VAR) e estatisticas. "
                                + "Os dados sao ingeridos da API-Football.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Bruno Brasil")
                                .url("https://github.com/BrunoBrasilJr/worldcup-api"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}