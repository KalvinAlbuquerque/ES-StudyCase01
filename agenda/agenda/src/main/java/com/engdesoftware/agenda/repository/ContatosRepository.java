package com.engdesoftware.agenda.repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Repository;

import com.engdesoftware.agenda.model.Contato;
import com.engdesoftware.agenda.model.IF_Contato;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

@Repository
public class ContatosRepository 
{
    public ContatosRepository() {}

    private static final String COLLECTION_NAME = "contatos";

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
        ApiFuture<QuerySnapshot> future = getDb().collection(COLLECTION_NAME).whereEqualTo("uid", uid).get();
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
        ApiFuture<QuerySnapshot> future = getDb().collection(COLLECTION_NAME)
                .whereEqualTo("uid", uid)
                .whereEqualTo("telefone", telefone)
                .get();

        for (QueryDocumentSnapshot document : future.get().getDocuments()) {
            getDb().collection(COLLECTION_NAME).document(document.getId()).delete().get();
        }
    }

    // MÉTODO CORRIGIDO
    public int removerContatosPorInicial(String uid, String inicial) throws InterruptedException, ExecutionException {
        Firestore db = getDb();
        String lowerBound = inicial;
        String upperBound = lowerBound + '\uf8ff';
        int deleteCount = 0;

        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                .whereEqualTo("uid", uid)
                .whereGreaterThanOrEqualTo("nome", lowerBound)
                .whereLessThanOrEqualTo("nome", upperBound)
                .get();

        // Itera sobre os resultados ("Gabriel", "Glenda") e deleta cada um
        for (QueryDocumentSnapshot document : future.get().getDocuments()) {
            db.collection(COLLECTION_NAME).document(document.getId()).delete();
            deleteCount++;
        }
        return deleteCount;
    }
}