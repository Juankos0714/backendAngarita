package com.example.adso.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * Configuración principal de Spring Security.
 * Define qué rutas están protegidas y cómo.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CRÍTICO: CORS debe ser lo primero
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                
                // Deshabilitamos CSRF porque usamos JWT (stateless)
                .csrf(csrf -> csrf.disable())

                // Definimos las reglas de autorización
                .authorizeHttpRequests(authz -> authz
                        // IMPORTANTE: OPTIONS debe estar PRIMERO y sin autenticación
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        
                        // Endpoints públicos (registro y login)
                        .requestMatchers("/api/auth/**").permitAll()
                        
                        // Endpoints de productos
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/products")
                            .hasAuthority(com.example.adso.model.Role.ADMIN.name())
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/products")
                            .hasAnyAuthority(
                                com.example.adso.model.Role.ADMIN.name(), 
                                com.example.adso.model.Role.USER.name()
                            )

                        // Todas las demás peticiones deben estar autenticadas
                        .anyRequest().authenticated()
                )

                // Gestión de sesiones STATELESS
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Proveedor de autenticación
                .authenticationProvider(authenticationProvider)

                // Filtro JWT ANTES del filtro de autenticación estándar
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuración CORS para Spring Security
     * NOTA: Esta configuración debe coincidir con CorsConfig
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Permitir cualquier origen (puedes restringir después)
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        
        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
        ));
        
        // Headers permitidos
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        
        // Permitir credenciales
        configuration.setAllowCredentials(true);
        
        // Headers expuestos
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization", "Content-Type"
        ));
        
        // Cache de preflight
        configuration.setMaxAge(3600L);
        
        // Aplicar a todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}