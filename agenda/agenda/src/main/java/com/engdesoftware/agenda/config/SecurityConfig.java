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
            .csrf(csrf -> csrf.disable()) // Desativa CSRF
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Define como stateless

            // Adiciona o nosso filtro personalizado ANTES do filtro padrão de autenticação
            .addFilterBefore(new FirebaseAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

            .authorizeHttpRequests(auth -> auth
                // Permite acesso público às nossas páginas e à API de login
                .requestMatchers("/login.html", "/agenda.html", "/api/auth/login").permitAll()
                // Exige que qualquer pedido para a API da agenda seja autenticado
                .requestMatchers("/api/agenda/**").authenticated()
                // Qualquer outro pedido pode ser negado por segurança
                .anyRequest().denyAll()
            );

        return http.build();
    }
}