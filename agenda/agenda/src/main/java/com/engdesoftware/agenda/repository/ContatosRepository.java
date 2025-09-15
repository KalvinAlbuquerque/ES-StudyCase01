package com.engdesoftware.agenda.repository;

import org.springframework.stereotype.Repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.engdesoftware.agenda.model.Contato;
import com.engdesoftware.agenda.model.IF_Contato;

import java.util.Collection;
import java.util.Map;

import java.util.concurrent.ExecutionException;

@Repository
public class ContatosRepository 
{
    public ContatosRepository() {}

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
    private Firestore getDb() 
    {
        return FirestoreClient.getFirestore();
    }

    public void save(String uid, Map<String, Object> dados) throws InterruptedException, ExecutionException
    {
        getDb().collection(COLLECTION_NAME).document().set(dados).get();
    }

    public void getAgenda(String uid, Collection<IF_Contato> contatos) throws InterruptedException, ExecutionException
    {
        // Constrói a query para buscar contatos pelo UID do usuário
        ApiFuture<QuerySnapshot> future = getDb().collection(COLLECTION_NAME).whereEqualTo("uid", uid).get();
        // Itera sobre os documentos retornados e os converte em objetos Contato
        for (QueryDocumentSnapshot document : future.get().getDocuments()) 
        {
            String nome = document.getString("nome");
            String telefone = document.getString("telefone");
            IF_Contato contato = new Contato(nome, telefone);
            contatos.add(contato);
        }
    }

    public void removerContato(String uid, String telefone) throws InterruptedException, ExecutionException
    {
        // Query para encontrar o documento específico a ser deletado
        ApiFuture<QuerySnapshot> future = getDb().collection(COLLECTION_NAME)
                .whereEqualTo("uid", uid)
                .whereEqualTo("telefone", telefone)
                .get();

        // Itera sobre os resultados (deve ser apenas um) e deleta o documento
        for (QueryDocumentSnapshot document : future.get().getDocuments()) {
            getDb().collection(COLLECTION_NAME).document(document.getId()).delete().get();
        }
    }
}
