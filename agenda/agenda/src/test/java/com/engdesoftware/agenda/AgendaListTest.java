package com.engdesoftware.agenda;

import com.engdesoftware.agenda.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgendaListTest {

    private IF_Agenda agenda;

    @BeforeEach
    void setUp() {
        agenda = new AgendaList();
    }

    @Test
    void deveAdicionarUmContatoComSucesso() {
        Contato novoContato = new Contato("Carlos", "1234-5678");
        boolean resultado = agenda.adicionaContato(novoContato);
        assertTrue(resultado);
        assertEquals(1, agenda.getListaAgenda().size());
        assertTrue(agenda.getListaAgenda().contains(novoContato));
    }

    @Test
    void naoDeveAdicionarContatoComTelefoneDuplicado() {
        Contato contato1 = new Contato("Carlos", "1234-5678");
        agenda.adicionaContato(contato1);
        

        assertThrows(IllegalArgumentException.class, () -> {
            Contato contato2 = new Contato("Maria", "1234-5678");
            agenda.adicionaContato(contato2);
        });

        assertEquals(1, agenda.getListaAgenda().size());
    }

    @Test
    void naoDeveAdicionarContatoComCamposObrigatoriosVazios() {

        assertThrows(IllegalArgumentException.class, () -> {
            agenda.adicionaContato(new Contato("", "1111-2222"));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            agenda.adicionaContato(new Contato("Ana", "  "));
        });

        assertTrue(agenda.getListaAgenda().isEmpty());
    }
    
    @Test
    void deveLocalizarUmContatoExistentePeloTelefone() {
        Contato contatoExistente = new Contato("Joana", "5555-4444");
        agenda.adicionaContato(contatoExistente);
        IF_Contato contatoEncontrado = agenda.getContato("5555-4444");
        assertNotNull(contatoEncontrado);
        assertEquals("Joana", contatoEncontrado.getNome());
    }
    
    @Test
    void deveRemoverUmContatoExistenteComSucesso() {
        Contato contatoParaRemover = new Contato("Pedro", "9876-5432");
        agenda.adicionaContato(contatoParaRemover);
        assertEquals(1, agenda.getListaAgenda().size());
        boolean resultado = agenda.removeContato("9876-5432");
        assertTrue(resultado);
        assertTrue(agenda.getListaAgenda().isEmpty());
    }

    @Test
    void naoDeveRemoverContatoInexistente() {
        Contato contato = new Contato("Pedro", "9876-5432");
        agenda.adicionaContato(contato);
        

        assertThrows(IllegalArgumentException.class, () -> {
            agenda.removeContato("0000-0000");
        });
        
        assertEquals(1, agenda.getListaAgenda().size());
    }
}