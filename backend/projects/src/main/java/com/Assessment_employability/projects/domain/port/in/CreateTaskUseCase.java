package com.Assessment_employability.projects.domain.port.in;

import com.Assessment_employability.projects.domain.model.Task;

import java.util.UUID;

/**
 * Input Port: CreateTaskUseCase
 * Defines the contract for creating a task.
 */
public interface CreateTaskUseCase {
    Task execute(UUID projectId, String title, UUID userId);
}
