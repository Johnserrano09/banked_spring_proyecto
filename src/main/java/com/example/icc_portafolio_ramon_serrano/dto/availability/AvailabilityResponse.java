package com.example.icc_portafolio_ramon_serrano.dto.availability;

import com.example.icc_portafolio_ramon_serrano.model.Modality;
import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AvailabilityResponse {

    private Long id;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Modality modality;
}
