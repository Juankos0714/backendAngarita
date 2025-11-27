package com.ubik.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Permitir credenciales
        config.setAllowCredentials(true);
        configuration.setAllowedOrigins(List.of(
        "http://localhost:4200", 
        "https://frontend-angarita.vercel.app", // Reemplaza con tu dominio real de Vercel
        "https://backendangarita.duckdns.org"  // Añade tu propio dominio seguro
    ));
        
// **MODIFICACIÓN 1: Usaremos '*' para prueba, o tu URL específica**
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        
        // Headers permitidos
        config.setAllowedHeaders(Arrays.asList(
            "Origin",
            "Content-Type",
            "Accept",
            "Authorization",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        
        // Métodos permitidos
        config.setAllowedMethods(Arrays.asList(
            "GET",
            "POST",
            "PUT",
            "DELETE",
            "OPTIONS"
        ));
        
// **MODIFICACIÓN 2: Aplicar a todas las rutas '/**'**
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}