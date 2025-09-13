package com.engdesoftware.agenda.controller;

import com.engdesoftware.agenda.service.AuthService;
import com.engdesoftware.agenda.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController 
{
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> handleLogin(@RequestBody LoginRequest loginRequest) 
    {
        Optional<String> tokenOptional = authService.loginAndGetIdToken(loginRequest.email(), loginRequest.password());

        return tokenOptional
                .map(token -> ResponseEntity.ok(Map.of("idToken", token)))
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of("error", "Unauthorized")));
    }
}
