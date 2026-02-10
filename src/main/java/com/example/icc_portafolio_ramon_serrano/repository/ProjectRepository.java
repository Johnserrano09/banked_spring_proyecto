package com.example.icc_portafolio_ramon_serrano.repository;

import com.example.icc_portafolio_ramon_serrano.model.Project;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByProgrammerId(Long programmerId);

    @Query("select count(p) from Project p where p.programmer.id = :programmerId and p.active = true")
    long countActiveByProgrammer(@Param("programmerId") Long programmerId);

    @Query("select count(p) from Project p where p.programmer.id = :programmerId")
    long countTotalByProgrammer(@Param("programmerId") Long programmerId);
}
