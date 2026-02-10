package com.example.icc_portafolio_ramon_serrano.dto.availability;

import com.example.icc_portafolio_ramon_serrano.model.Modality;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.Data;

@Data
public class AvailabilityRequest {

    @NotNull
    private DayOfWeek dayOfWeek;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotNull
    private Modality modality;
}
