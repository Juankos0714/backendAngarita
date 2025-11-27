package com.example.adso.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Permitir CUALQUIER origen (temporalmente para debugging)
                .allowedOriginPatterns("*")
                // Métodos HTTP permitidos
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
                // Headers permitidos
                .allowedHeaders("*")
                // Permitir credenciales (cookies, authorization headers, etc.)
                .allowCredentials(true)
                // Headers expuestos
                .exposedHeaders("Authorization", "Content-Type")
                // Cache de preflight por 1 hora
                .maxAge(3600);
    }
}