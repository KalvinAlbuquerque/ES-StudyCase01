package com.engdesoftware.agenda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Agenda.
 * 
 *  O SpringBootApplication é uma anotação que indica que esta classe é a classe principal
 *  da aplicação Spring Boot. Ela habilita várias configurações automáticas e componentes do Spring
 *  Boot, como a configuração de componentes, a varredura de componentes e a configuração
 *  automática de beans.
 * */
@SpringBootApplication
public class AgendaApplication {
	
	/**
	 * Método principal que inicia a aplicação Spring Boot.
	 * 
	 * Ele cria o contexto da aplicação Spring e inicia o servidor web embutido.
	 * 
	 * @param args Argumentos de linha de comando
	 * */
	public static void main(String[] args) {
		SpringApplication.run(AgendaApplication.class, args);
	}

}
