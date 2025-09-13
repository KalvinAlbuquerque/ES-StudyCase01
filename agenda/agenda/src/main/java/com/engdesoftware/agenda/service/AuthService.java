package com.engdesoftware.agenda.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService 
{
    @Value("${firebase.api.key}")
    private String firebaseApiKey;

    private static final String FIREBASE_AUTH_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=";

     /**
     * Tenta autenticar um usuário e retorna o ID Token se for bem-sucedido.
     * @param email O e-mail do usuário.
     * @param password A senha do usuário.
     * @return Um Optional contendo o idToken em caso de sucesso, ou vazio em caso de falha.
     */
    public Optional<String> loginAndGetIdToken(String email, String password)
    {
        RestTemplate restTemplate = new RestTemplate();
        String url = FIREBASE_AUTH_URL + firebaseApiKey;

        //
        //  Cria o body da requisição
        //
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);
        requestBody.put("returnSecureToken", true);

        //
        //  Configura os cabeçalhos
        //
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        //
        //  Faz a requisição POST
        //
        try
        {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            return Optional.ofNullable(root.path("idToken").asText(null));
        }
        catch (Exception e)
        {
            System.err.println("Erro na autenticação com Firebase: " + e.getMessage());
            return Optional.empty();
        }
    }

}
