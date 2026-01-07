package com.Assessment_employability.projects.domain.port.in;

import com.Assessment_employability.projects.domain.model.Project;

import java.util.List;
import java.util.UUID;

/**
 * Input Port: GetProjectsUseCase
 * Defines the contract for getting user projects.
 */
public interface GetProjectsUseCase {
    List<Project> execute(UUID userId);
}
