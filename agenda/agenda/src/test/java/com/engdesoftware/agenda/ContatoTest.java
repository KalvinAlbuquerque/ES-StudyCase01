package com.engdesoftware.agenda;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.engdesoftware.agenda.model.*;

class ContatoTest 
{

    @Test
    void doisContatosComMesmoTelefoneDevemSerIguais() {
        Contato contato1 = new Contato("Ana", "9999-8888");
        Contato contato2 = new Contato("Carlos", "9999-8888"); // Mesmo telefone, nome diferente

        // O método equals deve retornar true porque os telefones são iguais
        assertTrue(contato1.equals(contato2), "Contatos com o mesmo telefone devem ser considerados iguais.");
        // O hashCode também deve ser o mesmo
        assertEquals(contato1.hashCode(), contato2.hashCode(), "HashCodes de contatos iguais devem ser idênticos.");
    }

    @Test
    void doisContatosComTelefonesDiferentesNaoDevemSerIguais() {
        Contato contato1 = new Contato("Ana", "9999-8888");
        Contato contato2 = new Contato("Ana", "1111-2222"); // Mesmo nome, telefone diferente

        // O método equals deve retornar false
        assertFalse(contato1.equals(contato2), "Contatos com telefones diferentes não devem ser iguais.");
    }
    
    @Test
    void umContatoNaoDeveSerIgualANull() {
        Contato contato1 = new Contato("Ana", "9999-8888");
        
        // Compara o contato com null
        assertFalse(contato1.equals(null), "Um contato nunca deve ser igual a null.");
    }

    @Test
    void umContatoNaoDeveSerIgualAUmObjetoDeOutraClasse() {
        Contato contato1 = new Contato("Ana", "9999-8888");
        String texto = "9999-8888";

        // Compara o contato com um objeto de tipo diferente
        assertFalse(contato1.equals(texto), "Um contato não deve ser igual a um objeto de outra classe.");
    }
}