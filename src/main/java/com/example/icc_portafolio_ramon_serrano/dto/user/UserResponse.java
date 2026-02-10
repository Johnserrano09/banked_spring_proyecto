package com.example.icc_portafolio_ramon_serrano.dto.user;

import com.example.icc_portafolio_ramon_serrano.model.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private Role role;
}
