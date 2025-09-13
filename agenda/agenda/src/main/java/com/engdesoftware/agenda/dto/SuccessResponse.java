package com.engdesoftware.agenda.dto;

import java.time.LocalDateTime;

public record SuccessResponse(
    String message,
    LocalDateTime timestamp
) {}