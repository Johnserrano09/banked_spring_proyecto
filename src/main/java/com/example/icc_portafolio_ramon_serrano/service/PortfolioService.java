package com.example.icc_portafolio_ramon_serrano.service;

import com.example.icc_portafolio_ramon_serrano.dto.portfolio.ProjectRequest;
import com.example.icc_portafolio_ramon_serrano.dto.portfolio.ProjectResponse;
import com.example.icc_portafolio_ramon_serrano.dto.portfolio.SectionRequest;
import com.example.icc_portafolio_ramon_serrano.dto.portfolio.SectionResponse;
import com.example.icc_portafolio_ramon_serrano.model.PortfolioSection;
import com.example.icc_portafolio_ramon_serrano.model.Project;
import com.example.icc_portafolio_ramon_serrano.model.Role;
import com.example.icc_portafolio_ramon_serrano.model.User;
import com.example.icc_portafolio_ramon_serrano.repository.PortfolioSectionRepository;
import com.example.icc_portafolio_ramon_serrano.repository.ProjectRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PortfolioService {

    private final PortfolioSectionRepository sectionRepository;
    private final ProjectRepository projectRepository;
    private final UserService userService;

    public PortfolioService(
            PortfolioSectionRepository sectionRepository,
            ProjectRepository projectRepository,
            UserService userService) {
        this.sectionRepository = sectionRepository;
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    public SectionResponse createSection(SectionRequest request) {
        User programmer = requireProgrammer();

        PortfolioSection section = PortfolioSection.builder()
                .programmer(programmer)
                .title(request.getTitle())
                .description(request.getDescription())
                .displayOrder(request.getDisplayOrder())
                .build();

        return toSectionResponse(sectionRepository.save(section));
    }

    public List<SectionResponse> listSections(Long programmerId) {
        return sectionRepository.findByProgrammerId(programmerId).stream()
                .map(this::toSectionResponse)
                .toList();
    }

    public SectionResponse updateSection(Long sectionId, SectionRequest request) {
        User programmer = requireProgrammer();
        PortfolioSection section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Section not found"));
        if (!section.getProgrammer().getId().equals(programmer.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not owner of section");
        }

        section.setTitle(request.getTitle());
        section.setDescription(request.getDescription());
        section.setDisplayOrder(request.getDisplayOrder());
        return toSectionResponse(sectionRepository.save(section));
    }

    public void deleteSection(Long sectionId) {
        User programmer = requireProgrammer();
        PortfolioSection section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Section not found"));
        if (!section.getProgrammer().getId().equals(programmer.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not owner of section");
        }
        sectionRepository.delete(section);
    }

    public ProjectResponse createProject(ProjectRequest request) {
        User programmer = requireProgrammer();
        PortfolioSection section = null;
        if (request.getSectionId() != null) {
            section = sectionRepository.findById(request.getSectionId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Section not found"));
            if (!section.getProgrammer().getId().equals(programmer.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Section does not belong to programmer");
            }
        }

        Project project = Project.builder()
                .programmer(programmer)
                .section(section)
                .title(request.getTitle())
                .description(request.getDescription())
                .repoUrl(request.getRepoUrl())
                .demoUrl(request.getDemoUrl())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .active(Boolean.TRUE.equals(request.getActive()))
                .createdAt(LocalDateTime.now())
                .build();

        return toProjectResponse(projectRepository.save(project));
    }

    public ProjectResponse updateProject(Long projectId, ProjectRequest request) {
        User programmer = requireProgrammer();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        if (!project.getProgrammer().getId().equals(programmer.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not owner of project");
        }

        if (request.getSectionId() != null) {
            PortfolioSection section = sectionRepository.findById(request.getSectionId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Section not found"));
            if (!section.getProgrammer().getId().equals(programmer.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Section does not belong to programmer");
            }
            project.setSection(section);
        } else {
            project.setSection(null);
        }

        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setRepoUrl(request.getRepoUrl());
        project.setDemoUrl(request.getDemoUrl());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setActive(Boolean.TRUE.equals(request.getActive()));
        return toProjectResponse(projectRepository.save(project));
    }

    public void deleteProject(Long projectId) {
        User programmer = requireProgrammer();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        if (!project.getProgrammer().getId().equals(programmer.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not owner of project");
        }
        projectRepository.delete(project);
    }

    public List<ProjectResponse> listProjects(Long programmerId) {
        return projectRepository.findByProgrammerId(programmerId).stream()
                .map(this::toProjectResponse)
                .toList();
    }

    private User requireProgrammer() {
        User user = userService.getCurrentUser();
        if (user.getRole() != Role.PROGRAMMER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only programmers can perform this action");
        }
        return user;
    }

    private SectionResponse toSectionResponse(PortfolioSection section) {
        return SectionResponse.builder()
                .id(section.getId())
                .title(section.getTitle())
                .description(section.getDescription())
                .displayOrder(section.getDisplayOrder())
                .build();
    }

    private ProjectResponse toProjectResponse(Project project) {
        Long sectionId = project.getSection() != null ? project.getSection().getId() : null;
        return ProjectResponse.builder()
                .id(project.getId())
                .sectionId(sectionId)
                .title(project.getTitle())
                .description(project.getDescription())
                .repoUrl(project.getRepoUrl())
                .demoUrl(project.getDemoUrl())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .active(project.isActive())
                .build();
    }
}
