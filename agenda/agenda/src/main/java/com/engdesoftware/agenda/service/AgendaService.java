package com.engdesoftware.agenda.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.engdesoftware.agenda.model.FabricaAgenda;
import com.engdesoftware.agenda.model.IF_Agenda;
import com.engdesoftware.agenda.model.IF_Contato;
import com.engdesoftware.agenda.repository.ContatosRepository;

/**
 * Camada de serviço responsável pela lógica de negócio da agenda de contatos.
 * Esta classe atua como intermediária entre os Controllers e a persistência de dados no Firestore,
 * gerenciando operações como adicionar, remover e consultar contatos.
 *
 * @author Glenda Santana e Kalvin Albuquerque
 */
@Service
public class AgendaService 
{
    @Autowired
    private ContatosRepository contatosRepository;

    /**
     * Adiciona um novo contato à agenda de um usuário específico e persiste no banco de dados.
     * Primeiro, o contato é validado e adicionado à representação da agenda em memória.
     * Em seguida, é salvo como um novo documento no Firestore.
     *
     * @param uid     O identificador único (UID) do usuário proprietário do contato.
     * @param contato O objeto de contato a ser adicionado.
     * @return {@code true} se o contato foi adicionado com sucesso.
     * @throws InterruptedException     Se a thread for interrompida enquanto aguarda a operação do Firestore.
     * @throws ExecutionException       Se ocorrer um erro durante a execução da operação no Firestore.
     * @throws IllegalArgumentException Se os dados do contato forem inválidos (ex: nome/telefone vazios ou telefone duplicado).
     */
    public boolean adicionarContato(String uid, IF_Contato contato) throws InterruptedException, ExecutionException, IllegalArgumentException 
    {
        // 
        // Obtém a agenda atual do usuário para realizar validações
        // 
        IF_Agenda agenda = getAgendaDeUsuario(uid);

        // 
        // Valida e adiciona ao modelo local
        // 
        agenda.adicionaContato(contato); 

        // Prepara os dados para salvar no Firestore
        Map<String, Object> dadosParaSalvar = new HashMap<>();
        dadosParaSalvar.put("nome", contato.getNome());
        dadosParaSalvar.put("telefone", contato.getTelefone());
        dadosParaSalvar.put("uid", uid);

        contatosRepository.save(uid, dadosParaSalvar);
        return true;
    }

    /**
     * Recupera todos os contatos de um usuário específico do Firestore.
     * Cria uma instância de agenda e a popula com os contatos encontrados no banco de dados
     * que correspondem ao UID do usuário fornecido.
     *
     * @param uid O identificador único (UID) do usuário cuja agenda deve ser recuperada.
     * @return Um objeto {@code IF_Agenda} contendo a lista de contatos do usuário.
     */
    public IF_Agenda getAgendaDeUsuario(String uid) throws InterruptedException, ExecutionException
    {
        IF_Agenda agenda = FabricaAgenda.getInstancia().criaAgenda(FabricaAgenda.AGENDA_LIST);
        Collection<IF_Contato> contatos = agenda.getListaAgenda();

        contatosRepository.getAgenda(uid, contatos);

        return agenda;
    }

    /**
     * Remove um contato da agenda de um usuário, identificado pelo número de telefone.
     * A remoção ocorre tanto na representação em memória quanto no documento correspondente no Firestore.
     *
     * @param uid      O identificador único (UID) do usuário proprietário do contato.
     * @param telefone O número de telefone do contato a ser removido, que atua como chave de remoção.
     * @return {@code true} se o contato foi removido com sucesso.
     * @throws InterruptedException     Se a thread for interrompida enquanto aguarda a operação do Firestore.
     * @throws ExecutionException       Se ocorrer um erro durante a execução da operação no Firestore.
     * @throws IllegalArgumentException Se nenhum contato com o telefone especificado for encontrado na agenda do usuário.
     */
    public boolean removerContatoDeUsuario(String uid, String telefone) throws InterruptedException, ExecutionException, IllegalArgumentException 
    {
        IF_Agenda agenda = getAgendaDeUsuario(uid);
        agenda.removeContato(telefone); // Valida se o contato existe e o remove do modelo local

        contatosRepository.removerContato(uid, telefone);
        return true;
    }
}