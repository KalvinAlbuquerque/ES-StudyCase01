package com.engdesoftware.agenda.model;

/**
 * Interface que define o contrato para um Contato na agenda.
 */
public interface IF_Contato {

    String getNome();
    void setNome(String nome);

    String getTelefone();
    void setTelefone(String telefone);

    String getId();
    void setId(String id);

    String getUid();
    void setUid(String uid);

    // O método equals é crucial para a regra de unicidade do telefone
    boolean equals(Object obj);

    // O método toString ajuda a exibir o contato de forma legível
    String toString();
}