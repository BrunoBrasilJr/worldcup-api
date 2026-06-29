package com.portfolio.worldcup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Adiciona o prefixo "/api/v1" automaticamente em TODOS os controllers
     * anotados com @RestController.
     *
     * Vantagem: os controllers nao precisam repetir "/api/v1" em cada @RequestMapping.
     * Qualquer controller novo ja nasce versionado.
     *
     * O Actuator (/actuator/*) e o H2 console (/h2-console) NAO sao afetados,
     * pois nao passam por este path matching de controllers MVC.
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api/v1",
                clazz -> clazz.isAnnotationPresent(RestController.class));
    }
}