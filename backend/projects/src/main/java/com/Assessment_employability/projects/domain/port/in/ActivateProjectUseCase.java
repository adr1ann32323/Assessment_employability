package com.Assessment_employability.projects.domain.port.in;

import com.Assessment_employability.projects.domain.model.Project;

import java.util.UUID;

/**
 * Input Port: ActivateProjectUseCase
 * Defines the contract for activating a project.
 */
public interface ActivateProjectUseCase {
    Project execute(UUID projectId, UUID userId);
}
