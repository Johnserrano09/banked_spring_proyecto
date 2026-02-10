package com.example.icc_portafolio_ramon_serrano.dto.report;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectReportItem {

    private Long programmerId;
    private String programmerName;
    private long activeCount;
    private long totalCount;
}
