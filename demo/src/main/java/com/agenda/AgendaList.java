package com.agenda;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementação da interface IF_Agenda que utiliza um ArrayList para
 * armazenar os contatos.
 */
public class AgendaList implements IF_Agenda {

    private List<IF_Contato> listaContato = new ArrayList<>();

    /**
     * Adiciona um contato à agenda, após validar os campos e a unicidade do telefone.
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

        // Validação de unicidade do telefone, percorrendo a lista 
        for (IF_Contato c : listaContato) {
            if (c.getTelefone().equals(contato.getTelefone())) {
                System.out.println("Erro: Já existe um contato com o telefone " + contato.getTelefone() + ". A operação foi rejeitada.");
                return false; // Rejeita a operação
            }
        }
        
        boolean sucesso = listaContato.add(contato);
        if (sucesso) {
            System.out.println("Contato adicionado com sucesso: " + contato); // Confirmação de sucesso 
        }
        return sucesso;
    }

    /**
     * Localiza um contato na agenda pelo número de telefone. 
     * @param telefone O telefone a ser buscado.
     * @return O objeto IF_Contato correspondente ou null se não for encontrado.
     */
    @Override
    public IF_Contato getContato(String telefone) {
        for (IF_Contato c : listaContato) {
            if (c.getTelefone().equals(telefone)) {
                return c; // Retorna o contato encontrado 
            }
        }
        return null; // Retorna null se não encontrar
    }

    /**
     * Remove um contato da agenda. 
     * @param telefone O telefone do contato a ser removido. 
     * @return true se o contato foi removido com sucesso, false caso contrário.
     */
    @Override
    public boolean removeContato(String telefone) {
        IF_Contato contatoParaRemover = getContato(telefone);
        
        // Confirma a existência do contato antes de remover 
        if (contatoParaRemover != null) {
            boolean sucesso = listaContato.remove(contatoParaRemover);
            if (sucesso) {
                System.out.println("Confirmação: Contato removido com sucesso."); // Confirmação de remoção
            }
            return sucesso;
        }
        
        System.out.println("Erro: Contato com telefone " + telefone + " não foi encontrado para remoção."); // Mensagem de erro 
        return false;
    }

    /**
     * Fornece uma visão completa de todos os contatos da agenda. 
     * @return Uma coleção com a lista de contatos. 
     */
    @Override
    public Collection<IF_Contato> getListaAgenda() {
        return this.listaContato;
    }
}