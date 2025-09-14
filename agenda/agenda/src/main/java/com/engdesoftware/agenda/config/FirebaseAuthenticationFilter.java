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

/**
 * Filtro de autenticação que verifica tokens do Firebase em pedidos HTTP.
 * Este filtro intercepta cada pedido, extrai o token de autenticação do cabeçalho,
 * valida-o usando o Firebase Admin SDK, e se for válido, cria um contexto de segurança
 * para o utilizador autenticado.
 */
public class FirebaseAuthenticationFilter extends OncePerRequestFilter 
{

    /**
     * Filtro de autenticação para validar tokens JWT do Firebase em cada requisição HTTP.
     *
     * Este filtro verifica o cabeçalho "Authorization" da requisição, valida o token usando o Firebase Admin SDK
     * e, se válido, autentica o usuário no contexto do Spring Security. Caso o token seja inválido, o contexto
     * de segurança é limpo.
     *
     *
     * @param request  a requisição HTTP recebida
     * @param response a resposta HTTP a ser enviada
     * @param filterChain a cadeia de filtros do Spring Security
     * @throws ServletException se ocorrer um erro relacionado ao servlet
     * @throws IOException se ocorrer um erro de I/O durante o processamento da requisição
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        //
        //  Inspeciona o pedido que acabou de chegar e procura por um cabeçalho (header) chamado Authorization.
        //
        String authHeader = request.getHeader("Authorization");
        
        //
        //  Se o cabeçalho existe e se ele começa com a palavra Bearer (que é o formato padrão para tokens JWT), então
        //  extrai o token de ID do cabeçalho.
        //
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            
            String idToken = authHeader.substring(7);
            
            //
            //  Tenta verificar o token usando o Firebase Admin SDK.
            //  Se o token for válido, cria um objeto de autenticação do Spring Security
            //  e coloca-o no contexto de segurança.
            //  Se o token for inválido, limpa o contexto de segurança e lança uma exceção.
            //
            try {
                //
                //  Usa a instância ativa do Firebase Auth para verificar o token. Se o token for válido,
                //  devolve o token decodificado, que contém informações sobre o utilizador.
                //
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);

                //
                //  A partir do token decodificado, é extraído o uid (identificador único do utilizador no Firebase).
                //
                String uid = decodedToken.getUid();
                
                //
                //  Se o uid não for nulo...
                //
                if (uid != null) {

                    //
                    //  É criado um objeto de autenticação padrão do Spring Security.
                    //  É como dizer ao Spring: "Temos um utilizador autenticado. Seu 'nome de utilizador' é uid, ele não tem 
                    //  senha (porque a autenticação foi feita via token), e ele não tem permissões especiais (authorities vazias)."
                    //
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(uid, null, new ArrayList<>());

                    //
                    //  Coloca o objeto de autenticação no contexto de segurança do Spring para esse pedido específico. A partir desse momento
                    //  até o final do processamento deste pedido, o Spring sabe que o utilizador está autenticado.
                    //
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Se o token for inválido, limpa o contexto de segurança
                SecurityContextHolder.clearContext();
            }
        }

        // Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }
}