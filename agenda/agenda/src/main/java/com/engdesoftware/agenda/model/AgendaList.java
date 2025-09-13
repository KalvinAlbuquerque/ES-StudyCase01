package com.engdesoftware.agenda.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação da interface IF_Agenda que utiliza um ArrayList em memória
 * para armazenar os contatos. Esta versão foi atualizada para suportar
 * a assinatura de múltiplos utilizadores.
 */
public class AgendaList implements IF_Agenda {

    private final List<IF_Contato> listaContatoGlobal = new ArrayList<>();

    /**
     * Adiciona um contato à lista em memória, associando-o a um utilizador.
     * @param uid O identificador único do utilizador dono do contato.
     * @param contato O contato a ser adicionado.
     * @return true se o contato foi adicionado com sucesso, false caso contrário.
     */
    @Override
    public boolean adicionaContato(String uid, IF_Contato contato) {
        if (contato.getNome() == null || contato.getNome().trim().isEmpty() ||
            contato.getTelefone() == null || contato.getTelefone().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome e telefone são campos obrigatórios.");
        }

        // Verifica a unicidade do telefone apenas entre os contatos do mesmo utilizador
        boolean telefoneJaExiste = listaContatoGlobal.stream()
                .anyMatch(c -> c.getUid().equals(uid) && c.getTelefone().equals(contato.getTelefone()));

        if (telefoneJaExiste) {
            throw new IllegalArgumentException("Você já possui um contato com o telefone " + contato.getTelefone() + ".");
        }
        
        // Define o dono do contato antes de o adicionar
        contato.setUid(uid);
        return listaContatoGlobal.add(contato);
    }

    /**
     * Localiza um contato específico de um utilizador pelo telefone.
     * @param uid O identificador do utilizador.
     * @param telefone O telefone a ser buscado.
     * @return O objeto IF_Contato correspondente ou null se não for encontrado.
     */
    @Override
    public IF_Contato getContato(String uid, String telefone) {
        return listaContatoGlobal.stream()
                .filter(c -> c.getUid().equals(uid) && c.getTelefone().equals(telefone))
                .findFirst()
                .orElse(null);
    }

    /**
     * Remove um contato específico de um utilizador.
     * @param uid O identificador do utilizador.
     * @param telefone O telefone do contato a ser removido.
     * @return true se o contato foi removido com sucesso, false caso contrário.
     */
    @Override
    public boolean removeContato(String uid, String telefone) {
        IF_Contato contatoParaRemover = getContato(uid, telefone);
        if (contatoParaRemover != null) {
            return listaContatoGlobal.remove(contatoParaRemover);
        }
        return false;
    }

    /**
     * Retorna a lista de contatos pertencentes apenas a um utilizador específico.
     * @param uid O identificador do utilizador.
     * @return Uma coleção com os contatos do utilizador.
     */
    @Override
    public Collection<IF_Contato> getListaAgenda(String uid) {
        // Filtra a lista global para devolver apenas os contatos do utilizador solicitado
        return this.listaContatoGlobal.stream()
                .filter(contato -> contato.getUid().equals(uid))
                .collect(Collectors.toList());
    }
}