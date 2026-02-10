package com.example.icc_portafolio_ramon_serrano.repository;

import com.example.icc_portafolio_ramon_serrano.model.Role;
import com.example.icc_portafolio_ramon_serrano.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
}
