package com.example.icc_portafolio_ramon_serrano.controller;

import com.example.icc_portafolio_ramon_serrano.dto.availability.AvailabilityRequest;
import com.example.icc_portafolio_ramon_serrano.dto.availability.AvailabilityResponse;
import com.example.icc_portafolio_ramon_serrano.service.AvailabilityService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping("/programmers/{programmerId}")
    public ResponseEntity<List<AvailabilityResponse>> listSlots(@PathVariable Long programmerId) {
        return ResponseEntity.ok(availabilityService.listSlots(programmerId));
    }

    @PreAuthorize("hasRole('PROGRAMMER')")
    @PostMapping
    public ResponseEntity<AvailabilityResponse> createSlot(@Valid @RequestBody AvailabilityRequest request) {
        return ResponseEntity.ok(availabilityService.createSlot(request));
    }

    @PreAuthorize("hasRole('PROGRAMMER')")
    @PutMapping("/{slotId}")
    public ResponseEntity<AvailabilityResponse> updateSlot(
            @PathVariable Long slotId,
            @Valid @RequestBody AvailabilityRequest request) {
        return ResponseEntity.ok(availabilityService.updateSlot(slotId, request));
    }

    @PreAuthorize("hasRole('PROGRAMMER')")
    @DeleteMapping("/{slotId}")
    public ResponseEntity<Void> deleteSlot(@PathVariable Long slotId) {
        availabilityService.deleteSlot(slotId);
        return ResponseEntity.noContent().build();
    }
}
