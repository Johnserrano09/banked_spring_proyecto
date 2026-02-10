package com.example.icc_portafolio_ramon_serrano.controller;

import com.example.icc_portafolio_ramon_serrano.dto.user.UserResponse;
import com.example.icc_portafolio_ramon_serrano.service.UserService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/programmers")
public class ProgrammerController {

    private final UserService userService;

    public ProgrammerController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> listProgrammers() {
        return ResponseEntity.ok(userService.listProgrammers());
    }
}
