package com.engdesoftware.agenda.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.engdesoftware.agenda.model.FabricaAgenda;
import com.engdesoftware.agenda.model.IF_Agenda;

@Configuration
public class AgendaConfig 
{
    @Bean
    public IF_Agenda agenda() 
    {
        return FabricaAgenda.getInstancia().criaAgenda(FabricaAgenda.AGENDA_MAP);
    }
}