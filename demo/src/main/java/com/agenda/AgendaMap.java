package com.agenda;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementação da interface IF_Agenda que utiliza um HashMap para
 * armazenar os contatos, usando o telefone como chave.
 */
public class AgendaMap implements IF_Agenda {

    private Map<String, IF_Contato> listaContato = new HashMap<>();

    /**
     * Adiciona um contato à agenda, utilizando a eficiência do HashMap para
     * validar a unicidade do telefone.
     * @param contato O contato a ser adicionado.
     * @return true se o contato foi adicionado com sucesso, false caso contrário.
     */
    @Override
    public boolean adicionaContato(IF_Contato contato) {
        // Validação para não ter campos obrigatórios vazios 
        if (contato.getNome() == null || contato.getNome().trim().isEmpty() ||
            contato.getTelefone() == null || contato.getTelefone().trim().isEmpty()) {
            System.out.println("Erro: Nome e telefone são campos obrigatórios e não podem estar vazios.");
            return false;
        }

        // A verificação de unicidade é feita em tempo constante com o Map 
        if (listaContato.containsKey(contato.getTelefone())) {
            System.out.println("Erro: Já existe um contato com o telefone " + contato.getTelefone() + ". A operação foi rejeitada."); 
            return false;
        }

        listaContato.put(contato.getTelefone(), contato);
        System.out.println("Confirmação: Contato adicionado com sucesso: " + contato); 
        return true;
    }

    /**
     * Localiza um contato na agenda pelo número de telefone de forma eficiente.
     * @param telefone O telefone a ser buscado.
     * @return O objeto IF_Contato correspondente ou null se não for encontrado.
     */
    @Override
    public IF_Contato getContato(String telefone) {
        return listaContato.get(telefone); 
    }

    /**
     * Remove um contato da agenda.
     * @param telefone O telefone do contato a ser removido.
     * @return true se o contato foi removido com sucesso, false caso contrário.
     */
    @Override
    public boolean removeContato(String telefone) {
        // Confirma a existência do contato antes de remover 
        if (listaContato.containsKey(telefone)) {
            listaContato.remove(telefone);
            System.out.println("Confirmação: Contato removido com sucesso."); 
            return true;
        }
        
        System.out.println("Erro: Contato com telefone " + telefone + " não foi encontrado para remoção."); 
        return false;
    }

    /**
     * Fornece uma visão completa de todos os contatos da agenda.
     * @return Uma coleção com a lista de contatos.
     */
    @Override
    public Collection<IF_Contato> getListaAgenda() {
        return listaContato.values();
    }
}