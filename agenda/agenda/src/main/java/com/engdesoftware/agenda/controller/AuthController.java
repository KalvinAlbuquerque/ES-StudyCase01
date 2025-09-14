package com.engdesoftware.agenda.controller;

import com.engdesoftware.agenda.service.AuthService;
import com.engdesoftware.agenda.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST para autenticação de utilizadores.
 * Este controlador expõe um endpoint para login, que recebe credenciais do utilizador,
 * autentica-as usando o serviço AuthService, e retorna um token de ID do Firebase se a autenticação for bem-sucedida.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController 
{
    /*
     * Serviço de autenticação que lida com a lógica de login e geração de tokens.
     */
    @Autowired
    private AuthService authService;

    /**
     * Endpoint de login que autentica o utilizador e retorna um token de ID do Firebase.
     *
     * Este método recebe um objeto LoginRequest contendo o email e a password do utilizador,
     * tenta autenticar o utilizador usando o AuthService, e se a autenticação for bem-sucedida,
     * retorna um token de ID do Firebase. Se a autenticação falhar, retorna um status 401 Unauthorized.
     *
     * @param loginRequest objeto contendo as credenciais do utilizador (email e password)
     * @return ResponseEntity contendo o token de ID do Firebase ou uma mensagem de erro
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> handleLogin(@RequestBody LoginRequest loginRequest) 
    {
        Optional<String> tokenOptional = authService.loginAndGetIdToken(loginRequest.email(), loginRequest.password());

        return tokenOptional
                .map(token -> ResponseEntity.ok(Map.of("idToken", token)))
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of("error", "Unauthorized")));
    }
}
