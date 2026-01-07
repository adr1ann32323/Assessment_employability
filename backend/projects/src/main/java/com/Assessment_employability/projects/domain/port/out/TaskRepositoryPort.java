package com.Assessment_employability.projects.domain.port.out;

import com.Assessment_employability.projects.domain.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output Port: TaskRepositoryPort
 * Defines persistence operations for tasks.
 */
public interface TaskRepositoryPort {
    Task save(Task task);
    Optional<Task> findById(UUID id);
    List<Task> findAllByProjectId(UUID projectId);
    List<Task> findAllByProjectIdAndDeletedFalse(UUID projectId);
    long countActiveTasksByProjectId(UUID projectId);
}
