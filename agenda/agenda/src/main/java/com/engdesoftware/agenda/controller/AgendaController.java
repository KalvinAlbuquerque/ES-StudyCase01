package com.engdesoftware.agenda.controller;

import com.engdesoftware.agenda.model.Contato;
import com.engdesoftware.agenda.model.IF_Agenda;
import com.engdesoftware.agenda.model.IF_Contato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // <-- Importante
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/agenda")
public class AgendaController 
{

    @Autowired
    private IF_Agenda agenda;

    // O Spring Security irá injetar o objeto de autenticação que criámos no nosso filtro
    @GetMapping("/contatos")
    public ResponseEntity<Collection<IF_Contato>> listarContatos(Authentication authentication) {
        String uid = authentication.getName(); // O nome do principal é o UID que definimos
        return ResponseEntity.ok(agenda.getListaAgenda(uid));
    }

    @PostMapping("/contatos")
    public ResponseEntity<String> adicionarContato(Authentication authentication, @RequestBody Contato contato) {
        String uid = authentication.getName();
        agenda.adicionaContato(uid, contato);
        return ResponseEntity.ok("{\"message\":\"Contacto adicionado com sucesso.\"}");
    }

    @DeleteMapping("/contatos/{telefone}")
    public ResponseEntity<String> removerContato(Authentication authentication, @PathVariable String telefone) {
        String uid = authentication.getName();
        boolean sucesso = agenda.removeContato(uid, telefone);
        if (sucesso) {
            return ResponseEntity.ok("{\"message\":\"Contacto removido com sucesso.\"}");
        } else {
            // Lança uma exceção que será tratada pelo GlobalExceptionHandler
            throw new IllegalArgumentException("Contacto com o telefone " + telefone + " não encontrado.");
        }
    }
}