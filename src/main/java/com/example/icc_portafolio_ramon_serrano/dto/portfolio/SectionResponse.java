package com.example.icc_portafolio_ramon_serrano.dto.portfolio;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionResponse {

    private Long id;
    private String title;
    private String description;
    private Integer displayOrder;
}
