package com.engdesoftware.agenda.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.engdesoftware.agenda.model.Contato;
import com.engdesoftware.agenda.model.FabricaAgenda;
import com.engdesoftware.agenda.model.IF_Agenda;
import com.engdesoftware.agenda.model.IF_Contato;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

/**
 * Camada de serviço responsável pela lógica de negócio da agenda de contatos.
 * Esta classe atua como intermediária entre os Controllers e a persistência de dados no Firestore,
 * gerenciando operações como adicionar, remover e consultar contatos.
 *
 * @author Glenda Santana e Kalvin Albuquerque
 */
@Service
public class AgendaService {

    /**
     * Nome da coleção no Firestore onde os contatos são armazenados.
     */
    private static final String COLLECTION_NAME = "contatos";

    /**
     * Obtém a instância do cliente Firestore.
     * Método auxiliar privado para centralizar o acesso ao banco de dados.
     *
     * @return A instância do Firestore configurada.
     */
    private Firestore getDb() {
        return FirestoreClient.getFirestore();
    }

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
        dadosParaSalvar.put("uid", uid); // Armazena o UID do proprietário para futuras consultas

        // Executa a operação de escrita no banco de dados de forma síncrona
        getDb().collection(COLLECTION_NAME).document().set(dadosParaSalvar).get();

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
    public IF_Agenda getAgendaDeUsuario(String uid) {
        IF_Agenda agenda = FabricaAgenda.getInstancia().criaAgenda(FabricaAgenda.AGENDA_LIST);
        Collection<IF_Contato> contatos = agenda.getListaAgenda();

        try {
            // Constrói a query para buscar contatos pelo UID do usuário
            ApiFuture<QuerySnapshot> future = getDb().collection(COLLECTION_NAME).whereEqualTo("uid", uid).get();
            // Itera sobre os documentos retornados e os converte em objetos Contato
            for (QueryDocumentSnapshot document : future.get().getDocuments()) {
                Contato contato = document.toObject(Contato.class);
                contatos.add(contato);
            }
        } catch (Exception e) {
            // Em caso de falha, imprime o erro no console (idealmente, usar um logger)
            e.printStackTrace();
        }

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
    public boolean removerContatoDeUsuario(String uid, String telefone) throws InterruptedException, ExecutionException, IllegalArgumentException {
        IF_Agenda agenda = getAgendaDeUsuario(uid);
        agenda.removeContato(telefone); // Valida se o contato existe e o remove do modelo local

        // Query para encontrar o documento específico a ser deletado
        ApiFuture<QuerySnapshot> future = getDb().collection(COLLECTION_NAME)
                .whereEqualTo("uid", uid)
                .whereEqualTo("telefone", telefone)
                .get();

        // Itera sobre os resultados (deve ser apenas um) e deleta o documento
        for (QueryDocumentSnapshot document : future.get().getDocuments()) {
            getDb().collection(COLLECTION_NAME).document(document.getId()).delete().get();
        }

        return true;
    }
}