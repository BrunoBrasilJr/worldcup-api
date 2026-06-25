package com.portfolio.worldcup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    // @EnableScheduling liga o agendador do Spring.
    // Sem isto, qualquer metodo @Scheduled seria ignorado.
}