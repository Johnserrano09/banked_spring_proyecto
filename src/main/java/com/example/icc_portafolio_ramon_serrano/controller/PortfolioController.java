package com.example.icc_portafolio_ramon_serrano.controller;

import com.example.icc_portafolio_ramon_serrano.dto.portfolio.ProjectRequest;
import com.example.icc_portafolio_ramon_serrano.dto.portfolio.ProjectResponse;
import com.example.icc_portafolio_ramon_serrano.dto.portfolio.SectionRequest;
import com.example.icc_portafolio_ramon_serrano.dto.portfolio.SectionResponse;
import com.example.icc_portafolio_ramon_serrano.service.PortfolioService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping("/programmers/{programmerId}/sections")
    public ResponseEntity<List<SectionResponse>> listSections(@PathVariable Long programmerId) {
        return ResponseEntity.ok(portfolioService.listSections(programmerId));
    }

    @GetMapping("/programmers/{programmerId}/projects")
    public ResponseEntity<List<ProjectResponse>> listProjects(@PathVariable Long programmerId) {
        return ResponseEntity.ok(portfolioService.listProjects(programmerId));
    }

    @PreAuthorize("hasRole('PROGRAMMER')")
    @PostMapping("/sections")
    public ResponseEntity<SectionResponse> createSection(@Valid @RequestBody SectionRequest request) {
        return ResponseEntity.ok(portfolioService.createSection(request));
    }

    @PreAuthorize("hasRole('PROGRAMMER')")
    @PutMapping("/sections/{sectionId}")
    public ResponseEntity<SectionResponse> updateSection(
            @PathVariable Long sectionId,
            @Valid @RequestBody SectionRequest request) {
        return ResponseEntity.ok(portfolioService.updateSection(sectionId, request));
    }

    @PreAuthorize("hasRole('PROGRAMMER')")
    @DeleteMapping("/sections/{sectionId}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long sectionId) {
        portfolioService.deleteSection(sectionId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('PROGRAMMER')")
    @PostMapping("/projects")
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(portfolioService.createProject(request));
    }

    @PreAuthorize("hasRole('PROGRAMMER')")
    @PutMapping("/projects/{projectId}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(portfolioService.updateProject(projectId, request));
    }

    @PreAuthorize("hasRole('PROGRAMMER')")
    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        portfolioService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }
}
