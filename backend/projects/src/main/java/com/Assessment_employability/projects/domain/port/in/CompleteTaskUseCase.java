package com.Assessment_employability.projects.domain.port.in;

import com.Assessment_employability.projects.domain.model.Task;

import java.util.UUID;

/**
 * Input Port: CompleteTaskUseCase
 * Defines the contract for completing a task.
 */
public interface CompleteTaskUseCase {
    Task execute(UUID taskId, UUID userId);
}
