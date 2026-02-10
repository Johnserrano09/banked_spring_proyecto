package com.example.icc_portafolio_ramon_serrano.service;

import com.example.icc_portafolio_ramon_serrano.dto.advisory.AdvisoryRequest;
import com.example.icc_portafolio_ramon_serrano.dto.advisory.AdvisoryResponse;
import com.example.icc_portafolio_ramon_serrano.dto.advisory.AdvisoryStatusRequest;
import com.example.icc_portafolio_ramon_serrano.model.Advisory;
import com.example.icc_portafolio_ramon_serrano.model.AdvisoryStatus;
import com.example.icc_portafolio_ramon_serrano.model.Role;
import com.example.icc_portafolio_ramon_serrano.model.User;
import com.example.icc_portafolio_ramon_serrano.repository.AdvisoryRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdvisoryService {

    private final AdvisoryRepository advisoryRepository;
    private final UserService userService;
    private final NotificationDispatcher notificationDispatcher;

    public AdvisoryService(
            AdvisoryRepository advisoryRepository,
            UserService userService,
            NotificationDispatcher notificationDispatcher) {
        this.advisoryRepository = advisoryRepository;
        this.userService = userService;
        this.notificationDispatcher = notificationDispatcher;
    }

    @Transactional
    public AdvisoryResponse createAdvisory(AdvisoryRequest request) {
        User requester = userService.getCurrentUser();
        if (requester.getRole() != Role.USER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only users can request advisories");
        }

        User programmer = userService.getUserById(request.getProgrammerId());
        if (programmer.getRole() != Role.PROGRAMMER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Target user is not a programmer");
        }

        Advisory advisory = Advisory.builder()
                .programmer(programmer)
                .user(requester)
                .scheduledAt(request.getScheduledAt())
                .modality(request.getModality())
                .status(AdvisoryStatus.PENDING)
                .notes(request.getNotes())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .reminderSent(false)
                .build();

        Advisory saved = advisoryRepository.save(advisory);
        notificationDispatcher.notifyAdvisoryCreated(saved);
        return toResponse(saved);
    }

    @Transactional
    public AdvisoryResponse updateStatus(Long advisoryId, AdvisoryStatusRequest request) {
        User actor = userService.getCurrentUser();
        Advisory advisory = advisoryRepository.findById(advisoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Advisory not found"));

        boolean isProgrammerOwner = actor.getRole() == Role.PROGRAMMER
                && advisory.getProgrammer().getId().equals(actor.getId());
        boolean isAdmin = actor.getRole() == Role.ADMIN;
        if (!isProgrammerOwner && !isAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to update this advisory");
        }

        advisory.setStatus(request.getStatus());
        advisory.setUpdatedAt(LocalDateTime.now());
        Advisory saved = advisoryRepository.save(advisory);
        notificationDispatcher.notifyAdvisoryStatusChanged(saved);
        return toResponse(saved);
    }

    public List<AdvisoryResponse> listForProgrammer(Long programmerId) {
        return advisoryRepository.findByProgrammerId(programmerId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<AdvisoryResponse> listForUser(Long userId) {
        return advisoryRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<AdvisoryResponse> listMyAdvisories() {
        User current = userService.getCurrentUser();
        if (current.getRole() == Role.PROGRAMMER) {
            return listForProgrammer(current.getId());
        }
        return listForUser(current.getId());
    }

    @Transactional
    public void markReminderSent(Advisory advisory) {
        advisory.setReminderSent(true);
        advisory.setUpdatedAt(LocalDateTime.now());
        advisoryRepository.save(advisory);
    }

    private AdvisoryResponse toResponse(Advisory advisory) {
        return AdvisoryResponse.builder()
                .id(advisory.getId())
                .programmerId(advisory.getProgrammer().getId())
                .programmerName(advisory.getProgrammer().getFullName())
                .userId(advisory.getUser().getId())
                .userName(advisory.getUser().getFullName())
                .scheduledAt(advisory.getScheduledAt())
                .modality(advisory.getModality())
                .status(advisory.getStatus())
                .notes(advisory.getNotes())
                .build();
    }
}
