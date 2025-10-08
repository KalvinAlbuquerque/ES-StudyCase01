package com.engdesoftware.agenda.controller;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.engdesoftware.agenda.dto.ContatoRequest;
import com.engdesoftware.agenda.model.IF_Contato;
import com.engdesoftware.agenda.service.AgendaService;

/*
 * Controlador REST para gerir a agenda de contactos.
 * Fornece endpoints para listar, adicionar e remover contactos.
 * A autenticação é gerida pelo Spring Security.
 */
@RestController
@RequestMapping("/api/agenda")
public class AgendaController 
{

    @Autowired
    private AgendaService agendaService;

    /**
     * Retorna a lista de contatos do usuário autenticado.
     *
     * @param authentication objeto de autenticação contendo informações do usuário.
     * @return ResponseEntity contendo a coleção de contatos do usuário.
     */
    @GetMapping("/contatos")
    public ResponseEntity<Collection<IF_Contato>> listarContatos(Authentication authentication) 
    {
        try 
        {
            String uid = authentication.getName();
            return ResponseEntity.ok(agendaService.getAgendaDeUsuario(uid).getListaAgenda());
        } 
        catch (Exception e) 
        {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Adiciona um novo contato à agenda do usuário autenticado.
     *
     * @param authentication objeto de autenticação contendo informações do usuário.
     * @param contato objeto Contato a ser adicionado.
     * @return ResponseEntity com mensagem de sucesso em formato JSON.
     */
    @PostMapping("/contatos")
    public ResponseEntity<String> adicionarContato(Authentication authentication, @RequestBody ContatoRequest contato) throws InterruptedException, ExecutionException, IllegalArgumentException
    {
        String uid = authentication.getName();
        agendaService.adicionarContato(uid, contato.toContato());
        return ResponseEntity.ok("{\"message\":\"contato adicionado com sucesso.\"}");
    }

    @DeleteMapping("/contatos/{telefone}")
    public ResponseEntity<String> removerContato(Authentication authentication, @PathVariable String telefone) throws InterruptedException, ExecutionException, IllegalArgumentException
    {
        String uid = authentication.getName();
        boolean sucesso = agendaService.removerContatoDeUsuario(uid, telefone);

        if (sucesso) 
        {
            return ResponseEntity.ok("{\"message\":\"contato removido com sucesso.\"}");
        } 
        else 
        {
            throw new IllegalArgumentException("contato com o telefone " + telefone + " não encontrado.");
        }
    }

    // Endpoint para LISTAR contatos por inicial (para pré-visualização)
    @GetMapping("/contatos-por-inicial/{inicial}")
    public ResponseEntity<Collection<IF_Contato>> listarContatosPorInicial(Authentication authentication, @PathVariable String inicial) 
            throws InterruptedException, ExecutionException {
        String uid = authentication.getName();
        Collection<IF_Contato> contatos = agendaService.getContatosPorInicial(uid, inicial);
        return ResponseEntity.ok(contatos);
    }

    // Endpoint para APAGAR contatos por inicial
    @DeleteMapping("/contatos-por-inicial/{inicial}")
    public ResponseEntity<Map<String, String>> removerContatosPorInicial(Authentication authentication, @PathVariable String inicial) 
            throws InterruptedException, ExecutionException, IllegalArgumentException {
        String uid = authentication.getName();
        int count = agendaService.removerContatosPorInicial(uid, inicial);

        String message = count + " contato(s) com a inicial '" + inicial + "' foram removidos com sucesso.";
        return ResponseEntity.ok(Map.of("message", message));
    }
}