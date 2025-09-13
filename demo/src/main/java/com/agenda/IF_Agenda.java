package com.agenda;

import java.util.Collection;

/**
 * Interface que define as operações que uma Agenda deve suportar.
 */
public interface IF_Agenda {

    /**
     * Adiciona um novo contato na agenda.
     * @param contato O contato a ser adicionado.
     * @return true se o contato foi adicionado com sucesso, false caso contrário.
     */
    boolean adicionaContato(IF_Contato contato);

    /**
     * Localiza um contato na agenda pelo número de telefone.
     * @param telefone O telefone a ser buscado.
     * @return O objeto IF_Contato correspondente ou null se não for encontrado.
     */
    IF_Contato getContato(String telefone);

    /**
     * Remove um contato da agenda usando o telefone como identificador.
     * @param telefone O telefone do contato a ser removido.
     * @return true se o contato foi removido com sucesso, false caso contrário.
     */
    boolean removeContato(String telefone);

    /**
     * Retorna todos os contatos da agenda.
     * @return Uma coleção com todos os contatos.
     */
    Collection<IF_Contato> getListaAgenda();
}