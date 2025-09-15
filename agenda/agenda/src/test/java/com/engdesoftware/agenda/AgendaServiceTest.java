package com.engdesoftware.agenda;

import com.engdesoftware.agenda.model.Contato;
import com.engdesoftware.agenda.service.AgendaService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Teste simplificado para a camada de serviço (AgendaService).
 * O objetivo é testar a lógica de persistência de forma clara,
 * escondendo a complexidade da simulação do Firestore em métodos auxiliares.
 */
@ExtendWith(MockitoExtension.class)
class AgendaServiceTest {

    // --- Nossos "Dublês" (Mocks) ---
    @Mock private Firestore firestore;
    @Mock private CollectionReference collectionReference;
    @Mock private DocumentReference documentReference;
    @Mock private ApiFuture<WriteResult> writeResultApiFuture;
    
    // A classe que estamos realmente testando.
    // O Mockito vai injetar os dublês acima nela.
    @InjectMocks
    private AgendaService agendaService;

    // Mock estático para a chamada FirestoreClient.getFirestore()
    private MockedStatic<FirestoreClient> firestoreClientMockedStatic;

    // --- Configuração Padrão para os Testes ---
    @BeforeEach
    void setUp() {
        // Intercepta a chamada `FirestoreClient.getFirestore()` e a faz retornar nosso dublê `firestore`.
        firestoreClientMockedStatic = mockStatic(FirestoreClient.class);
        firestoreClientMockedStatic.when(FirestoreClient::getFirestore).thenReturn(firestore);
        // Diz que, sempre que o código pedir a coleção "contatos", deve retornar nosso dublê `collectionReference`.
        when(firestore.collection(any(String.class))).thenReturn(collectionReference);
    }

    @AfterEach
    void tearDown() {
        // Libera o mock estático após cada teste.
        firestoreClientMockedStatic.close();
    }

    // --- Os Testes ---

    @Test
    void deveSalvarContatoQuandoNaoExisteDuplicata() throws Exception {
        // 1. PREPARAÇÃO (Arrange)
        // O cenário: O banco de dados está vazio para este usuário.
        prepararBancoParaNaoEncontrarContatos("user123");

        // Preparamos o comando de escrita para funcionar.
        when(collectionReference.document()).thenReturn(documentReference);
        when(documentReference.set(any(Map.class))).thenReturn(writeResultApiFuture);

        // 2. AÇÃO (Act)
        // Executamos a função que queremos testar.
        boolean resultado = agendaService.adicionarContato("user123", new Contato("Ana", "111"));

        // 3. VERIFICAÇÃO (Assert)
        // O método nos disse que deu tudo certo?
        assertTrue(resultado);
        // O método realmente tentou salvar algo no banco de dados?
        verify(documentReference, times(1)).set(any(Map.class));
    }

    @Test
    void naoDeveSalvarContatoQuandoTelefoneJaExiste() throws Exception {
        // 1. PREPARAÇÃO (Arrange)
        // O cenário: O banco de dados já tem um contato ("Carlos") para este usuário.
        Contato contatoExistente = new Contato("Carlos", "222");
        prepararBancoParaEncontrarContatos("user123", List.of(contatoExistente));

        // 2. AÇÃO e VERIFICAÇÃO (Act & Assert)
        // Verificamos se a AÇÃO de adicionar um contato com o mesmo telefone "222"
        // lança o erro que estamos esperando.
        assertThrows(IllegalArgumentException.class, () -> {
            agendaService.adicionarContato("user123", new Contato("Maria", "222"));
        });

        // Verificação extra: garantimos que o método de salvar NUNCA foi chamado.
        verify(documentReference, never()).set(any(Map.class));
    }


    // --- Métodos Auxiliares (A Mágica para Simplificar) ---

    /**
     * Prepara a simulação do Firestore para retornar uma lista de contatos quando pesquisar por um uid.
     * @param uid O ID do usuário para a simulação da busca.
     * @param contatosARetornar A lista de contatos que a busca simulada deve encontrar.
     */
    private void prepararBancoParaEncontrarContatos(String uid, List<Contato> contatosARetornar) throws Exception {
        // Esta parte é complexa, mas agora está isolada aqui.
        Query queryMock = mock(Query.class);
        ApiFuture<QuerySnapshot> futureMock = mock(ApiFuture.class);
        QuerySnapshot querySnapshotMock = mock(QuerySnapshot.class);
        
        // Convertendo nossa lista de Contatos para uma lista de Documentos simulados.
        List<QueryDocumentSnapshot> docs = contatosARetornar.stream().map(contato -> {
            QueryDocumentSnapshot docMock = mock(QueryDocumentSnapshot.class);
            when(docMock.getString("nome")).thenReturn(contato.getNome());
            when(docMock.getString("telefone")).thenReturn(contato.getTelefone());
            return docMock;
        }).toList();

        // Configurando o roteiro da simulação
        when(collectionReference.whereEqualTo("uid", uid)).thenReturn(queryMock);
        when(queryMock.get()).thenReturn(futureMock);
        when(futureMock.get()).thenReturn(querySnapshotMock);
        when(querySnapshotMock.getDocuments()).thenReturn(docs);
    }

    /**
     * Prepara a simulação do Firestore para não encontrar nenhum contato para um uid.
     * É um atalho para o método acima, passando uma lista vazia.
     */
    private void prepararBancoParaNaoEncontrarContatos(String uid) throws Exception {
        prepararBancoParaEncontrarContatos(uid, Collections.emptyList());
    }
}