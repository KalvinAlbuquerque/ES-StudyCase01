package com.engdesoftware.agenda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.engdesoftware.agenda.model.*;
import static org.junit.jupiter.api.Assertions.*;

class AgendaListTest {

    private IF_Agenda agenda;
    private final String testUid = "test-user-123"; // UID de teste para o utilizador

    @BeforeEach
    void setUp() {
        agenda = new AgendaList();
    }

    @Test
    void deveAdicionarUmContatoComSucesso() {
        Contato novoContato = new Contato("Carlos", "1234-5678");
        // Passa o UID do utilizador de teste ao adicionar
        boolean resultado = agenda.adicionaContato(testUid, novoContato);

        assertTrue(resultado);
        assertEquals(1, agenda.getListaAgenda(testUid).size());
        assertTrue(agenda.getListaAgenda(testUid).contains(novoContato));
    }

    @Test
    void naoDeveAdicionarContatoComTelefoneDuplicado() {
        Contato contato1 = new Contato("Carlos", "1234-5678");
        Contato contato2 = new Contato("Maria", "1234-5678");

        agenda.adicionaContato(testUid, contato1);
        // Tenta adicionar o segundo contato para o mesmo utilizador
        assertThrows(IllegalArgumentException.class, () -> {
            agenda.adicionaContato(testUid, contato2);
        });
        assertEquals(1, agenda.getListaAgenda(testUid).size());
    }

    @Test
    void deveLocalizarUmContatoExistentePeloTelefone() {
        Contato contatoExistente = new Contato("Joana", "5555-4444");
        agenda.adicionaContato(testUid, contatoExistente);

        IF_Contato contatoEncontrado = agenda.getContato(testUid, "5555-4444");

        assertNotNull(contatoEncontrado);
        assertEquals("Joana", contatoEncontrado.getNome());
    }

    @Test
    void deveRemoverUmContatoExistenteComSucesso() {
        Contato contatoParaRemover = new Contato("Pedro", "9876-5432");
        agenda.adicionaContato(testUid, contatoParaRemover);
        assertEquals(1, agenda.getListaAgenda(testUid).size());

        boolean resultado = agenda.removeContato(testUid, "9876-5432");

        assertTrue(resultado);
        assertTrue(agenda.getListaAgenda(testUid).isEmpty());
    }
}