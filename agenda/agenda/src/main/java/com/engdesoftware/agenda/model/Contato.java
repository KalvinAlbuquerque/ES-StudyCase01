package com.engdesoftware.agenda.model;

import java.util.Objects;

/**
 * Classe que representa um contato na agenda.
 * Implementa a interface IF_Contato.
 */
public class Contato implements IF_Contato {
    private String id;

    
    private String nome;
    private String telefone;

    /**
     * Construtor para criar um novo Contato.
     * @param nome O nome do contato.
     * @param telefone O telefone do contato (usado como identificador único).
     */
    public Contato(String nome, String telefone) {
        this.nome = nome;
        this.telefone = telefone;
    }

    public Contato() {}

    @Override
    public String getNome() {
        return this.nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String getTelefone() {
        return this.telefone;
    }

    @Override
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }


    /**
     * Compara este contato com outro objeto.
     * A comparação é baseada apenas no telefone, garantindo a unicidade.
     * @param obj O objeto a ser comparado.
     * @return true se os telefones forem iguais, false caso contrário.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Contato contato = (Contato) obj;
        return this.telefone.equals(contato.telefone);
    }
    
    /**
     * Gera um código hash baseado apenas no telefone.
     * É uma boa prática sobrescrever hashCode quando se sobrescreve equals.
     */
    @Override
    public int hashCode() {
        return Objects.hash(telefone);
    }

    /**
     * Retorna uma representação em String do objeto Contato
     * Útil para exibir informações do contato.
     */
    @Override
    public String toString() {
        return "Contato{nome='" + nome + "', telefone='" + telefone + "'}";
    }
}