package com.example.icc_portafolio_ramon_serrano.repository;

import com.example.icc_portafolio_ramon_serrano.model.Advisory;
import com.example.icc_portafolio_ramon_serrano.model.AdvisoryStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvisoryRepository extends JpaRepository<Advisory, Long> {
    List<Advisory> findByProgrammerId(Long programmerId);
    List<Advisory> findByUserId(Long userId);
    List<Advisory> findByStatus(AdvisoryStatus status);
    List<Advisory> findByStatusAndScheduledAtBetween(AdvisoryStatus status, LocalDateTime start, LocalDateTime end);
    List<Advisory> findByStatusAndReminderSentFalseAndScheduledAtBetween(
            AdvisoryStatus status,
            LocalDateTime start,
            LocalDateTime end);
}
