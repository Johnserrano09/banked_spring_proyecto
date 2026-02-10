package com.example.icc_portafolio_ramon_serrano.service;

import com.example.icc_portafolio_ramon_serrano.dto.report.AdvisoryProgrammerReportItem;
import com.example.icc_portafolio_ramon_serrano.dto.report.AdvisoryStatusReportItem;
import com.example.icc_portafolio_ramon_serrano.dto.report.ProjectReportItem;
import com.example.icc_portafolio_ramon_serrano.model.Advisory;
import com.example.icc_portafolio_ramon_serrano.model.AdvisoryStatus;
import com.example.icc_portafolio_ramon_serrano.model.User;
import com.example.icc_portafolio_ramon_serrano.repository.AdvisoryRepository;
import com.example.icc_portafolio_ramon_serrano.repository.ProjectRepository;
import com.example.icc_portafolio_ramon_serrano.repository.UserRepository;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final AdvisoryRepository advisoryRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ReportService(
            AdvisoryRepository advisoryRepository,
            ProjectRepository projectRepository,
            UserRepository userRepository) {
        this.advisoryRepository = advisoryRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public List<AdvisoryStatusReportItem> advisoryStatusReport() {
        return Arrays.stream(AdvisoryStatus.values())
                .map(status -> AdvisoryStatusReportItem.builder()
                        .status(status)
                        .count(advisoryRepository.findByStatus(status).size())
                        .build())
                .toList();
    }

    public List<AdvisoryProgrammerReportItem> advisoryByProgrammer(LocalDateTime start, LocalDateTime end) {
        return userRepository.findByRole(com.example.icc_portafolio_ramon_serrano.model.Role.PROGRAMMER).stream()
                .map(programmer -> {
                    long count = advisoryRepository
                            .findByStatusAndScheduledAtBetween(AdvisoryStatus.CONFIRMED, start, end)
                            .stream()
                            .filter(advisory -> advisory.getProgrammer().getId().equals(programmer.getId()))
                            .count();
                    return AdvisoryProgrammerReportItem.builder()
                            .programmerId(programmer.getId())
                            .programmerName(programmer.getFullName())
                            .count(count)
                            .build();
                })
                .toList();
    }

    public List<ProjectReportItem> projectReport() {
        return userRepository.findByRole(com.example.icc_portafolio_ramon_serrano.model.Role.PROGRAMMER).stream()
                .map(programmer -> ProjectReportItem.builder()
                        .programmerId(programmer.getId())
                        .programmerName(programmer.getFullName())
                        .activeCount(projectRepository.countActiveByProgrammer(programmer.getId()))
                        .totalCount(projectRepository.countTotalByProgrammer(programmer.getId()))
                        .build())
                .toList();
    }

    public byte[] buildAdvisoryPdf(List<Advisory> advisories) {
        try {
            Document document = new Document();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(new Paragraph("Reporte de asesorias"));
            PdfPTable table = new PdfPTable(5);
            table.addCell("ID");
            table.addCell("Programador");
            table.addCell("Usuario");
            table.addCell("Fecha");
            table.addCell("Estado");
            for (Advisory advisory : advisories) {
                table.addCell(String.valueOf(advisory.getId()));
                table.addCell(advisory.getProgrammer().getFullName());
                table.addCell(advisory.getUser().getFullName());
                table.addCell(String.valueOf(advisory.getScheduledAt()));
                table.addCell(advisory.getStatus().name());
            }
            document.add(table);
            document.close();
            return outputStream.toByteArray();
        } catch (DocumentException ex) {
            throw new IllegalStateException("Unable to generate PDF", ex);
        }
    }

    public byte[] buildProjectPdf(List<ProjectReportItem> items) {
        try {
            Document document = new Document();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(new Paragraph("Reporte de proyectos"));
            PdfPTable table = new PdfPTable(4);
            table.addCell("Programador");
            table.addCell("Activos");
            table.addCell("Total");
            table.addCell("Fecha");
            for (ProjectReportItem item : items) {
                table.addCell(item.getProgrammerName());
                table.addCell(String.valueOf(item.getActiveCount()));
                table.addCell(String.valueOf(item.getTotalCount()));
                table.addCell(String.valueOf(LocalDateTime.now()));
            }
            document.add(table);
            document.close();
            return outputStream.toByteArray();
        } catch (DocumentException ex) {
            throw new IllegalStateException("Unable to generate PDF", ex);
        }
    }
}
