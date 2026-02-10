package com.example.icc_portafolio_ramon_serrano.dto.portfolio;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectResponse {

    private Long id;
    private Long sectionId;
    private String title;
    private String description;
    private String repoUrl;
    private String demoUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
}
