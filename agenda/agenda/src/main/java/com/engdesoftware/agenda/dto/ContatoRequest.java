package com.engdesoftware.agenda.dto;

import com.engdesoftware.agenda.model.IF_Contato;
import com.engdesoftware.agenda.model.Contato;

public record ContatoRequest (
    String nome,
    String telefone
) 
{
    public IF_Contato toContato() 
    {
        return new Contato(nome, telefone);
    }
}
