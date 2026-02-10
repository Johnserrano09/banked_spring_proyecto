package com.example.icc_portafolio_ramon_serrano.dto.portfolio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SectionRequest {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Integer displayOrder;
}
