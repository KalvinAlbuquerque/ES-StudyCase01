package com.engdesoftware.agenda.dto;

public record LoginRequest (
    String email,
    String password
) {}
