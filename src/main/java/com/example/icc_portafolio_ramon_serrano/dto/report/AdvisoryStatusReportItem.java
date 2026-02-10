package com.example.icc_portafolio_ramon_serrano.dto.report;

import com.example.icc_portafolio_ramon_serrano.model.AdvisoryStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdvisoryStatusReportItem {

    private AdvisoryStatus status;
    private long count;
}
