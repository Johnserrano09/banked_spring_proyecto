package com.example.icc_portafolio_ramon_serrano.dto.portfolio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class ProjectRequest {

    private Long sectionId;

    @NotBlank
    private String title;

    private String description;

    private String repoUrl;

    private String demoUrl;

    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    private Boolean active;
}
