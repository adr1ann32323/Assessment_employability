package com.Assessment_employability.projects.application.service;

import com.Assessment_employability.projects.domain.model.Project;
import com.Assessment_employability.projects.domain.port.in.CreateProjectUseCase;
import com.Assessment_employability.projects.domain.port.out.ProjectRepositoryPort;

import java.util.UUID;

/**
 * Use Case: Create Project
 * Implements the logic to create a new project.
 */
public class CreateProjectService implements CreateProjectUseCase {

    private final ProjectRepositoryPort projectRepository;

    public CreateProjectService(ProjectRepositoryPort projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project execute(UUID ownerId, String name) {
        // Create the project in DRAFT status
        Project project = Project.create(ownerId, name);

        // Persist the project
        return projectRepository.save(project);
    }
}
