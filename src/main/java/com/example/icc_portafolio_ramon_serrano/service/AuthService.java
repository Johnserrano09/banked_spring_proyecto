package com.example.icc_portafolio_ramon_serrano.service;

import com.example.icc_portafolio_ramon_serrano.dto.auth.AuthResponse;
import com.example.icc_portafolio_ramon_serrano.dto.auth.LoginRequest;
import com.example.icc_portafolio_ramon_serrano.dto.auth.RegisterRequest;
import com.example.icc_portafolio_ramon_serrano.model.Role;
import com.example.icc_portafolio_ramon_serrano.model.User;
import com.example.icc_portafolio_ramon_serrano.repository.UserRepository;
import com.example.icc_portafolio_ramon_serrano.security.JwtService;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();

        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved.getEmail(), Map.of("role", saved.getRole().name()));

        return AuthResponse.builder()
                .token(token)
                .userId(saved.getId())
                .role(saved.getRole())
                .fullName(saved.getFullName())
                .email(saved.getEmail())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        String token = jwtService.generateToken(user.getEmail(), Map.of("role", user.getRole().name()));

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .role(user.getRole())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .build();
    }
}
