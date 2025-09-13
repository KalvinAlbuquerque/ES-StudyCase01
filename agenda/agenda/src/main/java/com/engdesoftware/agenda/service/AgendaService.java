package com.engdesoftware.agenda.service;

import com.engdesoftware.agenda.model.Contato;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AgendaService 
{
    @Value("${firebase.project.id}")
    private String projectId;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String getFirestoreBaseUrl() {
        return String.format("https://firestore.googleapis.com/v1/projects/%s/databases/(default)/documents", projectId);
    }

    public List<Contato> getContatos(String idToken) throws IOException {
        String url = getFirestoreBaseUrl() + "/contatos";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(idToken); 
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        List<Contato> contatos = new ArrayList<>();
        JsonNode root = objectMapper.readTree(response.getBody());
        if (root.has("documents")) {
            for (JsonNode doc : root.path("documents")) {
                JsonNode fields = doc.path("fields");
                String nome = fields.path("nome").path("stringValue").asText();
                String telefone = fields.path("telefone").path("stringValue").asText();
                
                Contato contato = new Contato(nome, telefone);
                
                // Extrai o ID do documento da URL 'name'
                String docName = doc.path("name").asText();
                String docId = docName.substring(docName.lastIndexOf('/') + 1);
                contato.setId(docId);
                
                contatos.add(contato);
            }
        }

        return contatos;
    }

    public void adicionarContato(String idToken, Contato contato) {
        String url = getFirestoreBaseUrl() + "/contatos";

        // Monta o corpo da requisição no formato que o Firestore espera
        Map<String, Object> fields = Map.of(
            "nome", Map.of("stringValue", contato.getNome()),
            "telefone", Map.of("stringValue", contato.getTelefone())
        );
        Map<String, Object> requestBody = Map.of("fields", fields);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(idToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        restTemplate.postForEntity(url, entity, String.class);
    }

    public void removerContato(String idToken, String documentId) {
        String url = String.format("%s/contatos/%s", getFirestoreBaseUrl(), documentId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(idToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
    }
}
