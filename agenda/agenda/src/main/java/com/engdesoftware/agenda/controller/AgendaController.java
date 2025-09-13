package com.engdesoftware.agenda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.engdesoftware.agenda.model.IF_Agenda;

@Controller
@RequestMapping("/agenda")
public class AgendaController 
{
    @Autowired
    private IF_Agenda agenda;

    /**
     * Mapeia a URL principal ("/") para exibir a lista de contatos.
     */
    @GetMapping
    public String paginaAgenda(Model model)
    {
        model.addAttribute("contatos", agenda.getListaAgenda());
        return "agenda";
    }

}
