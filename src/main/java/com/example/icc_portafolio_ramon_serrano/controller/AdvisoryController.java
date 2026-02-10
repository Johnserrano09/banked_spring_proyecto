package com.example.icc_portafolio_ramon_serrano.controller;

import com.example.icc_portafolio_ramon_serrano.dto.advisory.AdvisoryRequest;
import com.example.icc_portafolio_ramon_serrano.dto.advisory.AdvisoryResponse;
import com.example.icc_portafolio_ramon_serrano.dto.advisory.AdvisoryStatusRequest;
import com.example.icc_portafolio_ramon_serrano.service.AdvisoryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/advisories")
public class AdvisoryController {

    private final AdvisoryService advisoryService;

    public AdvisoryController(AdvisoryService advisoryService) {
        this.advisoryService = advisoryService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<AdvisoryResponse> create(@Valid @RequestBody AdvisoryRequest request) {
        return ResponseEntity.ok(advisoryService.createAdvisory(request));
    }

    @PreAuthorize("hasRole('PROGRAMMER') or hasRole('ADMIN')")
    @PutMapping("/{advisoryId}/status")
    public ResponseEntity<AdvisoryResponse> updateStatus(
            @PathVariable Long advisoryId,
            @Valid @RequestBody AdvisoryStatusRequest request) {
        return ResponseEntity.ok(advisoryService.updateStatus(advisoryId, request));
    }

    @PreAuthorize("hasRole('USER') or hasRole('PROGRAMMER')")
    @GetMapping("/mine")
    public ResponseEntity<List<AdvisoryResponse>> listMine() {
        return ResponseEntity.ok(advisoryService.listMyAdvisories());
    }
}
