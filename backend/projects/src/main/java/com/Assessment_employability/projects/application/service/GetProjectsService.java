package com.Assessment_employability.projects.application.service;

import com.Assessment_employability.projects.domain.model.Project;
import com.Assessment_employability.projects.domain.port.in.GetProjectsUseCase;
import com.Assessment_employability.projects.domain.port.out.ProjectRepositoryPort;

import java.util.List;
import java.util.UUID;

/**
 * Use Case: Get Projects
 * Implements the logic to get all projects for a user.
 */
public class GetProjectsService implements GetProjectsUseCase {

    private final ProjectRepositoryPort projectRepository;

    public GetProjectsService(ProjectRepositoryPort projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public List<Project> execute(UUID userId) {
        // Return only non-deleted projects
        return projectRepository.findAllByOwnerIdAndDeletedFalse(userId);
    }
}
