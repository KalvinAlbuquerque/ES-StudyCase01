package com.engdesoftware.agenda.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
 * Essa classe trabalha em conjunto com o FirebaseAuthenticationFilter para garantir que apenas utilizadores autenticados
 * possam aceder a determinados recursos da aplicação.
 * 
 * Configura o Spring Security lidar com autenticação e autorização. Define as "regras de trânsito" para todos os pedidos HTTP
 * que chegam à aplicação.
 * 
 * A notação @EnableWebSecurity ativa o suporte à segurança web do Spring e fornce a integração com o Spring MVC.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    
    /**
     * Configura a cadeia de filtros de segurança da aplicação usando o Spring Security.
     * 
     * Esta configuração:
     * - Desativa a proteção CSRF (adequado para APIs sem estado).
     * - Define o gerenciamento de sessão como stateless, pois a autenticação é feita via tokens do Firebase.
     * - Adiciona o filtro personalizado {@link FirebaseAuthenticationFilter} antes do {@link UsernamePasswordAuthenticationFilter} para processar a autenticação do Firebase.
     * - Permite acesso a todos os recursos estáticos comuns (ex: CSS, JS, imagens).
     * - Permite acesso não autenticado a páginas específicas, API de login e arquivos estáticos na raiz.
     * - Exige autenticação para todos os endpoints sob <code>/api/agenda/**</code>.
     * - Nega acesso a quaisquer outras requisições não explicitamente permitidas.
     *
     * @param http o {@link HttpSecurity} a ser modificado
     * @return o {@link SecurityFilterChain} configurado
     * @throws Exception se ocorrer algum erro durante a configuração
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            /*
             * Desativa a proteção CSRF (Cross-Site Request Forgery) pois o Firebase já lida com a segurança dos tokens.
             */
            .csrf(csrf -> csrf.disable())
            /*
             * Configura o gerenciamento de sessão para ser stateless, já que a autenticação é feita via tokens do Firebase.
             * Dessa forma, a apliacação não depende dos cookies de sessão do servidor e sim do token JWT enviado em cada requisição.
             */
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            /*
             * Insere o filtro personalizado do FirebaseAuthenticationFilter na cadeia de filtros de segurança. Ele é colocado
             * antes do filtro padrão de autenticação de nome de utilizador e senha. Isto significa que, para cada pedido que chega, o 
             * filtro do Firebase será executado primeiro para verificar se existe um token do Firebase válido.
             */
            .addFilterBefore(new FirebaseAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            /*
             * Define as regras de autorização para diferentes endpoints da aplicação.
             */
            .authorizeHttpRequests(auth -> auth

                /*
                 * Permite acesso público e sem autenticação a todas as páginas HTML/CSS principais e API de login.
                 */
                .requestMatchers(
                    "/", 
                    "/login.html", 
                    "/index.html", 
                    "/adicionar.html", 
                    "/remover.html",
                    "/api/auth/login",
                    "/*.png",
                    "/*.ico",
                    "/*.css"
                ).permitAll()
                
                /*
                 * Exige autenticação para acessar a api de agenda.
                 */
                .requestMatchers(
                    "/api/agenda/**"
                ).authenticated()
                
                /*
                 * Todos os outros pedidos são negados.
                 */
                .anyRequest().denyAll()
            );

        return http.build();
    }
}