package com.Assessment_employability.projects.infrastructure.adapter.in.web;

import com.Assessment_employability.projects.domain.model.Project;
import com.Assessment_employability.projects.domain.port.in.ActivateProjectUseCase;
import com.Assessment_employability.projects.domain.port.in.CreateProjectUseCase;
import com.Assessment_employability.projects.domain.port.in.GetProjectsUseCase;
import com.Assessment_employability.projects.infrastructure.adapter.in.web.dto.CreateProjectRequest;
import com.Assessment_employability.projects.infrastructure.adapter.in.web.dto.ProjectResponse;
import com.Assessment_employability.projects.infrastructure.adapter.in.web.mapper.ProjectDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST Controller for Projects
 * Handles project CRUD operations
 */
@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Endpoints for project management")
public class ProjectController {

    private final CreateProjectUseCase createProjectUseCase;
    private final GetProjectsUseCase getProjectsUseCase;
    private final ActivateProjectUseCase activateProjectUseCase;
    private final ProjectDtoMapper projectDtoMapper;

    public ProjectController(CreateProjectUseCase createProjectUseCase,
                             GetProjectsUseCase getProjectsUseCase,
                             ActivateProjectUseCase activateProjectUseCase,
                             ProjectDtoMapper projectDtoMapper) {
        this.createProjectUseCase = createProjectUseCase;
        this.getProjectsUseCase = getProjectsUseCase;
        this.activateProjectUseCase = activateProjectUseCase;
        this.projectDtoMapper = projectDtoMapper;
    }

    @PostMapping
    @Operation(summary = "Create a new project", description = "Creates a new project in DRAFT status")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody CreateProjectRequest request,
            Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        Project project = createProjectUseCase.execute(userId, request.name());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectDtoMapper.toResponse(project));
    }

    @GetMapping
    @Operation(summary = "Get all projects", description = "Returns all projects for the authenticated user")
    public ResponseEntity<List<ProjectResponse>> getProjects(Authentication authentication) {
        UUID userId = authentication != null ? (UUID) authentication.getPrincipal() : null;
        if (userId == null) {
            return ResponseEntity.ok(List.of());
        }
        List<ProjectResponse> projects = getProjectsUseCase.execute(userId)
                .stream()
                .map(projectDtoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a project", description = "Changes project status from DRAFT to ACTIVE")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ProjectResponse> activateProject(
            @PathVariable UUID id,
            Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        Project project = activateProjectUseCase.execute(id, userId);
        return ResponseEntity.ok(projectDtoMapper.toResponse(project));
    }
}

