package com.example.icc_portafolio_ramon_serrano.dto.advisory;

import com.example.icc_portafolio_ramon_serrano.model.Modality;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AdvisoryRequest {

    @NotNull
    private Long programmerId;

    @NotNull
    private LocalDateTime scheduledAt;

    @NotNull
    private Modality modality;

    private String notes;
}
