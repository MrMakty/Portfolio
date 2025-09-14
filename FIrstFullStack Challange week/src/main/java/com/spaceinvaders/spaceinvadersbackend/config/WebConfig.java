package com.spaceinvaders.spaceinvadersbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Alleen gehoste frontend toegang.

                //voor testen makkelijk als alles en iedereen toegang heeft, later aanpassen naar specifieke origin.
                .allowedOrigins("*")
                //.allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(false);  // Zet op false om geen credentials door te geven
    }
}
