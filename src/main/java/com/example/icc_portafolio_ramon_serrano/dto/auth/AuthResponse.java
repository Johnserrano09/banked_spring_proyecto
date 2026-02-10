package com.example.icc_portafolio_ramon_serrano.dto.auth;

import com.example.icc_portafolio_ramon_serrano.model.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private String token;
    private Long userId;
    private Role role;
    private String fullName;
    private String email;
}
