package com.engdesoftware.agenda.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
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
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(new FirebaseAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                // Regra 1: Permite acesso a todos os recursos estáticos comuns. (Forma moderna)
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                
                // Regra 2: Permite acesso às nossas páginas, API de login e arquivos estáticos na raiz.
                // (Forma moderna, sem AntPathRequestMatcher)
                .requestMatchers(
                    "/", 
                    "/login.html", 
                    "/index.html", 
                    "/adicionar.html", 
                    "/remover.html",
                    "/api/auth/login",
                    "/style.css", 
                    "/*.png", 
                    "/*.ico"
                ).permitAll()
                
                // Regra 3: Exige autenticação para a API da agenda.
                .requestMatchers("/api/agenda/**").authenticated()
                
                // Regra 4: Nega qualquer outro pedido.
                .anyRequest().denyAll()
            );

        return http.build();
    }
}