package com.example.icc_portafolio_ramon_serrano.controller;

import com.example.icc_portafolio_ramon_serrano.dto.report.AdvisoryProgrammerReportItem;
import com.example.icc_portafolio_ramon_serrano.dto.report.AdvisoryStatusReportItem;
import com.example.icc_portafolio_ramon_serrano.dto.report.ProjectReportItem;
import com.example.icc_portafolio_ramon_serrano.model.Advisory;
import com.example.icc_portafolio_ramon_serrano.model.AdvisoryStatus;
import com.example.icc_portafolio_ramon_serrano.repository.AdvisoryRepository;
import com.example.icc_portafolio_ramon_serrano.service.ReportService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;
    private final AdvisoryRepository advisoryRepository;

    public ReportController(ReportService reportService, AdvisoryRepository advisoryRepository) {
        this.reportService = reportService;
        this.advisoryRepository = advisoryRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/advisories/status")
    public ResponseEntity<List<AdvisoryStatusReportItem>> advisoryStatusReport() {
        return ResponseEntity.ok(reportService.advisoryStatusReport());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/advisories/programmer")
    public ResponseEntity<List<AdvisoryProgrammerReportItem>> advisoryByProgrammer(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(reportService.advisoryByProgrammer(start, end));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/projects")
    public ResponseEntity<List<ProjectReportItem>> projectReport() {
        return ResponseEntity.ok(reportService.projectReport());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/advisories/pdf")
    public ResponseEntity<byte[]> advisoryPdf(@RequestParam(defaultValue = "CONFIRMED") AdvisoryStatus status) {
        List<Advisory> advisories = advisoryRepository.findByStatus(status);
        byte[] pdf = reportService.buildAdvisoryPdf(advisories);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=advisories.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/projects/pdf")
    public ResponseEntity<byte[]> projectPdf() {
        byte[] pdf = reportService.buildProjectPdf(reportService.projectReport());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=projects.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
