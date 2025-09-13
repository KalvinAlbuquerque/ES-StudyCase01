package com.engdesoftware.agenda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.engdesoftware.agenda.model.*;
import static org.junit.jupiter.api.Assertions.*;

// Nota: Estes testes para AgendaMap funcionarão em memória,
// não irão conectar-se ao Firebase, o que é ideal para testes unitários rápidos.
class AgendaMapTest {

    private IF_Agenda agenda;
    private final String testUid = "test-user-123"; // UID de teste

    @BeforeEach
    void setUp() {
        // Para testar a implementação do Map em memória, usamos a classe diretamente.
        agenda = new AgendaList(); // Usamos AgendaList aqui para testes unitários em memória
    }

    @Test
    void deveAdicionarUmContatoComSucesso() {
        Contato novoContato = new Contato("Carlos", "1234-5678");
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