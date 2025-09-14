package com.engdesoftware.agenda.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

/**
 * Manipulador de exceções global para a API REST.
 * Captura exceções específicas e as converte em respostas HTTP formatadas em JSON,
 * garantindo que o front-end sempre receba um formato de erro consistente.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler 
{

    /**
     * Manipula exceções do tipo IllegalArgumentException.
     *
     * Quando um controller lança essa exceção (como no caso de um telefone duplicado),
     * este método a captura e retorna uma resposta HTTP 400 (Bad Request)
     * com um corpo JSON contendo a mensagem da exceção.
     *
     * @param ex A exceção IllegalArgumentException capturada.
     * @return Um ResponseEntity contendo o mapa com a mensagem de erro e o status HTTP 400.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> body = Map.of("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}