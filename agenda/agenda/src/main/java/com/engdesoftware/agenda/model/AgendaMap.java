package com.engdesoftware.agenda.model;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AgendaMap implements IF_Agenda {

    private static final String COLLECTION_NAME = "contatos";

    private Firestore getDb() {
        return FirestoreClient.getFirestore();
    }

    @Override
    public boolean adicionaContato(String uid, IF_Contato contato) {
        if (contato.getNome() == null || contato.getNome().trim().isEmpty() ||
            contato.getTelefone() == null || contato.getTelefone().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome e telefone são campos obrigatórios.");
        }
        try {
            // Verifica se o telefone já existe APENAS para este utilizador
            if (getContato(uid, contato.getTelefone()) != null) {
                throw new IllegalArgumentException("Você já possui um contacto com o telefone " + contato.getTelefone() + ".");
            }
            // Define o dono do contacto antes de o guardar
            contato.setUid(uid);
            getDb().collection(COLLECTION_NAME).document().set(contato).get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public IF_Contato getContato(String uid, String telefone) {
        try {
            // A query agora filtra por telefone E pelo uid do utilizador
            ApiFuture<QuerySnapshot> future = getDb().collection(COLLECTION_NAME)
                    .whereEqualTo("uid", uid)
                    .whereEqualTo("telefone", telefone)
                    .limit(1).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            if (!documents.isEmpty()) {
                QueryDocumentSnapshot doc = documents.get(0);
                Contato contatoEncontrado = doc.toObject(Contato.class);
                contatoEncontrado.setId(doc.getId());
                return contatoEncontrado;
            }
            return null;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean removeContato(String uid, String telefone) {
        try {
            // Localiza o contacto específico deste utilizador para obter o ID do documento
            IF_Contato contatoParaRemover = getContato(uid, telefone);
            if (contatoParaRemover == null || contatoParaRemover.getId() == null) {
                return false;
            }
            getDb().collection(COLLECTION_NAME).document(contatoParaRemover.getId()).delete().get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Collection<IF_Contato> getListaAgenda(String uid) {
        List<IF_Contato> contatos = new ArrayList<>();
        try {
            // A query agora filtra para trazer apenas os contactos cujo "uid" corresponde ao do utilizador logado
            ApiFuture<QuerySnapshot> future = getDb().collection(COLLECTION_NAME).whereEqualTo("uid", uid).get();
            for (QueryDocumentSnapshot document : future.get().getDocuments()) {
                Contato contato = document.toObject(Contato.class);
                contato.setId(document.getId());
                contatos.add(contato);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return contatos;
    }
}