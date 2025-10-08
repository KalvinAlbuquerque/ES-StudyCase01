package com.engdesoftware.agenda.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desabilita a proteção CSRF, que é a causa provável do erro 403
            .csrf(csrf -> csrf.disable())
            
            // Configura a sessão como "sem estado" (stateless)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Adiciona nosso filtro de autenticação Firebase
            .addFilterBefore(new FirebaseAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            
            // Define as regras de autorização para os endpoints
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/", "/login.html", "/index.html", "/adicionar.html", 
                    "/remover.html", "/api/auth/login", "/*.png", "/*.ico", "/*.css"
                ).permitAll() // Permite acesso público a estas páginas/recursos
                
                .requestMatchers("/api/agenda/**").authenticated() // Exige autenticação para qualquer endpoint da agenda
                
                .anyRequest().denyAll() // Nega todas as outras requisições
            );

        return http.build();
    }
}