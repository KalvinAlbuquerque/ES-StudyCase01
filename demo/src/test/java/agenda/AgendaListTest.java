package agenda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.agenda.AgendaList;
import com.agenda.Contato;
import com.agenda.IF_Agenda;
import com.agenda.IF_Contato;


import static org.junit.jupiter.api.Assertions.*;

class AgendaListTest {

    private IF_Agenda agenda;

    // A anotação @BeforeEach faz com que este método seja executado ANTES de cada @Test
    // Isso garante que cada teste comece com uma agenda nova e vazia.
    @BeforeEach
    void setUp() {
        agenda = new AgendaList();
    }

    @Test
    void deveAdicionarUmContatoComSucesso() {
        Contato novoContato = new Contato("Carlos", "1234-5678");
        boolean resultado = agenda.adicionaContato(novoContato);

        assertTrue(resultado, "A adição de um contato válido deve retornar true.");
        assertEquals(1, agenda.getListaAgenda().size(), "A agenda deve conter 1 contato após a adição.");
        assertTrue(agenda.getListaAgenda().contains(novoContato), "A agenda deve conter o contato que foi adicionado.");
    }

    @Test
    void naoDeveAdicionarContatoComTelefoneDuplicado() {
        Contato contato1 = new Contato("Carlos", "1234-5678");
        Contato contato2 = new Contato("Maria", "1234-5678"); // Mesmo telefone

        agenda.adicionaContato(contato1);
        boolean resultado = agenda.adicionaContato(contato2); // Tenta adicionar o segundo contato

        assertFalse(resultado, "A adição de um contato com telefone duplicado deve retornar false.");
        assertEquals(1, agenda.getListaAgenda().size(), "A agenda deve conter apenas 1 contato.");
    }

    @Test
    void naoDeveAdicionarContatoComCamposObrigatoriosVazios() {
        Contato contatoSemNome = new Contato("", "1111-2222");
        Contato contatoSemTelefone = new Contato("Ana", "  "); // Telefone com espaços

        boolean resultado1 = agenda.adicionaContato(contatoSemNome);
        boolean resultado2 = agenda.adicionaContato(contatoSemTelefone);

        assertFalse(resultado1, "Não deve adicionar contato com nome vazio.");
        assertFalse(resultado2, "Não deve adicionar contato com telefone vazio.");
        assertTrue(agenda.getListaAgenda().isEmpty(), "A agenda deve permanecer vazia.");
    }

    @Test
    void deveLocalizarUmContatoExistentePeloTelefone() {
        Contato contatoExistente = new Contato("Joana", "5555-4444");
        agenda.adicionaContato(contatoExistente);

        IF_Contato contatoEncontrado = agenda.getContato("5555-4444");

        assertNotNull(contatoEncontrado, "O contato encontrado não deve ser nulo.");
        assertEquals("Joana", contatoEncontrado.getNome(), "O nome do contato encontrado deve ser 'Joana'.");
    }

    @Test
    void deveRetornarNullAoLocalizarContatoInexistente() {
        IF_Contato contatoEncontrado = agenda.getContato("0000-0000");
        assertNull(contatoEncontrado, "A busca por um contato inexistente deve retornar null.");
    }
    
    @Test
    void deveRemoverUmContatoExistenteComSucesso() {
        Contato contatoParaRemover = new Contato("Pedro", "9876-5432");
        agenda.adicionaContato(contatoParaRemover);

        assertEquals(1, agenda.getListaAgenda().size()); // Verifica que o contato está na lista

        boolean resultado = agenda.removeContato("9876-5432");

        assertTrue(resultado, "A remoção de um contato existente deve retornar true.");
        assertTrue(agenda.getListaAgenda().isEmpty(), "A agenda deve ficar vazia após a remoção.");
    }

    @Test
    void naoDeveRemoverContatoInexistente() {
        Contato contato = new Contato("Pedro", "9876-5432");
        agenda.adicionaContato(contato);

        boolean resultado = agenda.removeContato("0000-0000"); // Tenta remover um telefone que não existe

        assertFalse(resultado, "A tentativa de remover um contato inexistente deve retornar false.");
        assertEquals(1, agenda.getListaAgenda().size(), "A agenda não deve ser alterada.");
    }
}