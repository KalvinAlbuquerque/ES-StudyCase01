package com.engdesoftware.agenda.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String idToken = authHeader.substring(7);

            try {
                // Verifica o token usando o Firebase Admin SDK
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
                String uid = decodedToken.getUid();

                // Se o token for válido, cria um objeto de autenticação do Spring Security
                if (uid != null) {
                    // O "principal" é o UID, e as "authorities" são as permissões (neste caso, vazias)
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(uid, null, new ArrayList<>());

                    // Coloca o objeto de autenticação no contexto de segurança
                    // Isto informa ao Spring que o utilizador está autenticado para este pedido
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Se o token for inválido, limpa o contexto de segurança
                SecurityContextHolder.clearContext();
                // Opcional: pode-se devolver uma resposta 401 Unauthorized aqui,
                // mas deixar o Spring tratar é mais consistente.
            }
        }

        // Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }
}