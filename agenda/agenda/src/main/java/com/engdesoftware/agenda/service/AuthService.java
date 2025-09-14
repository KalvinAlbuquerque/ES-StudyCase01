package com.engdesoftware.agenda.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Serviço para autenticação de usuários utilizando Firebase Authentication.
 */
@Service
public class AuthService 
{
    /* 
     * Chave da API do Firebase, injetada a partir do arquivo de propriedades. 
     * Certifique-se de configurar esta propriedade no application.properties ou application.yml.
     */
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
        //
        //  Cria um objeto RestTemplate, que é a principal feramenta do Spring para fazer requisições HTTP
        //
        RestTemplate restTemplate = new RestTemplate();

        //
        //  Monta o URL completo para a API de autenticação do Firebase
        //
        String url = FIREBASE_AUTH_URL + firebaseApiKey;

        //
        //  Cria um map que representa o corpo JSON do pedido
        //  De acordo com a documentação do Firebase, é necessário enviar o email, password e returnSecureToken (boolean) para fazer login
        //
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);
        requestBody.put("returnSecureToken", true);

        //
        //  Configura os cabeçalhos com application/json (informa ao firebase que estamos enviando dados em formato JSON)
        //
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        //
        //  Encapsula o corpo do pedido e os cabeçalhos num único objeto HttpEntity
        //
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        //
        //  Faz a requisição POST
        //
        try
        {
            //
            //  Se o login for bem sucedido, devolve uma resposta 200 e um corpo JSON contendo o idToken. Se as credenciais
            //  estiverem erradas, o Firebase devolve um erro 400 e o RestTemplate lança uma exceção.
            //
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            
            //
            //  Se o pedido for bem sucedido, este código usa a biblioteca Jackson para analisar a resposta JSON, navegar
            //  até o campo "idToken" e devolvê-lo encapsulado num Optional.
            //
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            return Optional.ofNullable(root.path("idToken").asText(null));
        }
        catch (Exception e)
        {
            //
            //  Se o RestTemplate lançar uma exceção, captura-a e devolve um Optional vazio
            //
            System.err.println("Erro na autenticação com Firebase: " + e.getMessage());
            return Optional.empty();
        }
    }

}
