package com.example.icc_portafolio_ramon_serrano.service;

import com.example.icc_portafolio_ramon_serrano.dto.availability.AvailabilityRequest;
import com.example.icc_portafolio_ramon_serrano.dto.availability.AvailabilityResponse;
import com.example.icc_portafolio_ramon_serrano.model.AvailabilitySlot;
import com.example.icc_portafolio_ramon_serrano.model.Role;
import com.example.icc_portafolio_ramon_serrano.model.User;
import com.example.icc_portafolio_ramon_serrano.repository.AvailabilitySlotRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AvailabilityService {

    private final AvailabilitySlotRepository availabilityRepository;
    private final UserService userService;

    public AvailabilityService(AvailabilitySlotRepository availabilityRepository, UserService userService) {
        this.availabilityRepository = availabilityRepository;
        this.userService = userService;
    }

    public AvailabilityResponse createSlot(AvailabilityRequest request) {
        User programmer = requireProgrammer();
        AvailabilitySlot slot = AvailabilitySlot.builder()
                .programmer(programmer)
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .modality(request.getModality())
                .build();

        return toResponse(availabilityRepository.save(slot));
    }

    public AvailabilityResponse updateSlot(Long slotId, AvailabilityRequest request) {
        User programmer = requireProgrammer();
        AvailabilitySlot slot = availabilityRepository.findById(slotId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Availability not found"));
        if (!slot.getProgrammer().getId().equals(programmer.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not owner of availability slot");
        }
        slot.setDayOfWeek(request.getDayOfWeek());
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());
        slot.setModality(request.getModality());
        return toResponse(availabilityRepository.save(slot));
    }

    public void deleteSlot(Long slotId) {
        User programmer = requireProgrammer();
        AvailabilitySlot slot = availabilityRepository.findById(slotId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Availability not found"));
        if (!slot.getProgrammer().getId().equals(programmer.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not owner of availability slot");
        }
        availabilityRepository.delete(slot);
    }

    public List<AvailabilityResponse> listSlots(Long programmerId) {
        return availabilityRepository.findByProgrammerId(programmerId).stream()
                .map(this::toResponse)
                .toList();
    }

    private User requireProgrammer() {
        User user = userService.getCurrentUser();
        if (user.getRole() != Role.PROGRAMMER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only programmers can perform this action");
        }
        return user;
    }

    private AvailabilityResponse toResponse(AvailabilitySlot slot) {
        return AvailabilityResponse.builder()
                .id(slot.getId())
                .dayOfWeek(slot.getDayOfWeek())
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .modality(slot.getModality())
                .build();
    }
}
