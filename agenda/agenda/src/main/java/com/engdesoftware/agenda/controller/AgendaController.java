package com.engdesoftware.agenda.controller;

import com.engdesoftware.agenda.model.IF_Contato;
import com.engdesoftware.agenda.service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.engdesoftware.agenda.dto.ContatoRequest;

import java.util.Collection;
import java.util.concurrent.ExecutionException;

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
            //
            //  O "nome" definido no objeto de autenticação é o uid do utilizador. Com esta linha, o Controller sabe
            //  quem é o utilizador que está fazendo requisição.
            //
            String uid = authentication.getName();

            //
            //  O controller delega a obtenção da lista de contactos ao serviço de agenda.
            //
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
        //
        //  O "nome" definido no objeto de autenticação é o uid do utilizador. Com esta linha, o Controller sabe
        //  quem é o utilizador que está fazendo requisição.
        //
        String uid = authentication.getName();

        //
        //  O controller delega a adição do contacto ao serviço de agenda.
        //
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
}
