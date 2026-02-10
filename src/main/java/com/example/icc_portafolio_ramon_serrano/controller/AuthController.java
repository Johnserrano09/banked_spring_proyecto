package com.example.icc_portafolio_ramon_serrano.controller;

import com.example.icc_portafolio_ramon_serrano.dto.auth.AuthResponse;
import com.example.icc_portafolio_ramon_serrano.dto.auth.LoginRequest;
import com.example.icc_portafolio_ramon_serrano.dto.auth.RegisterRequest;
import com.example.icc_portafolio_ramon_serrano.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
