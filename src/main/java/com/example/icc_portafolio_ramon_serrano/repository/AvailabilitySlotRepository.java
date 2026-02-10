package com.example.icc_portafolio_ramon_serrano.repository;

import com.example.icc_portafolio_ramon_serrano.model.AvailabilitySlot;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {
    List<AvailabilitySlot> findByProgrammerId(Long programmerId);
}
