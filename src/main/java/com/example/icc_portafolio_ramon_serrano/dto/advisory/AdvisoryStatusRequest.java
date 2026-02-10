package com.example.icc_portafolio_ramon_serrano.dto.advisory;

import com.example.icc_portafolio_ramon_serrano.model.AdvisoryStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdvisoryStatusRequest {

    @NotNull
    private AdvisoryStatus status;
}
