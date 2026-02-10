package com.example.icc_portafolio_ramon_serrano.dto.advisory;

import com.example.icc_portafolio_ramon_serrano.model.AdvisoryStatus;
import com.example.icc_portafolio_ramon_serrano.model.Modality;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdvisoryResponse {

    private Long id;
    private Long programmerId;
    private String programmerName;
    private Long userId;
    private String userName;
    private LocalDateTime scheduledAt;
    private Modality modality;
    private AdvisoryStatus status;
    private String notes;
}
