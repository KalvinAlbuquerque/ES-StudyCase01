package com.engdesoftware.agenda.dto;

import java.time.LocalDateTime;

// Usa um record para uma representação concisa e imutável da resposta de erro.
public record ErrorResponse(
    int status,
    String message,
    LocalDateTime timestamp
) {}