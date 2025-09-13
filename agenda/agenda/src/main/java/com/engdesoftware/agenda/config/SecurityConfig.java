package com.engdesoftware.agenda.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig 
{
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desabilita CSRF para a API
            .authorizeHttpRequests(auth -> auth
                // Permite acesso à página de login e à API de autenticação
                .requestMatchers("/login.html", "/api/auth/login").permitAll()
                // Exige que qualquer outra requisição seja autenticada
                .anyRequest().authenticated()
            )
            // Redireciona usuários não autenticados para a página de login
            .formLogin(form -> form.loginPage("/login.html"));
            
        return http.build();
    }
}
