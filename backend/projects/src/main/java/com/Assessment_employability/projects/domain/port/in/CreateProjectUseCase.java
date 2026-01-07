package com.Assessment_employability.projects.domain.port.in;

import com.Assessment_employability.projects.domain.model.Project;

import java.util.UUID;

/**
 * Input Port: CreateProjectUseCase
 * Defines the contract for creating a project.
 */
public interface CreateProjectUseCase {
    Project execute(UUID ownerId, String name);
}
