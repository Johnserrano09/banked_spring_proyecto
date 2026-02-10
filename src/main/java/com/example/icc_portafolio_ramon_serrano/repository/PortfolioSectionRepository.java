package com.example.icc_portafolio_ramon_serrano.repository;

import com.example.icc_portafolio_ramon_serrano.model.PortfolioSection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioSectionRepository extends JpaRepository<PortfolioSection, Long> {
    List<PortfolioSection> findByProgrammerId(Long programmerId);
}
