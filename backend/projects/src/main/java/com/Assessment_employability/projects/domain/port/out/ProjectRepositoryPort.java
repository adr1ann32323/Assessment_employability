package com.Assessment_employability.projects.domain.port.out;

import com.Assessment_employability.projects.domain.model.Project;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output Port: ProjectRepositoryPort
 * Defines persistence operations for projects.
 */
public interface ProjectRepositoryPort {
    Project save(Project project);
    Optional<Project> findById(UUID id);
    List<Project> findAllByOwnerId(UUID ownerId);
    List<Project> findAllByOwnerIdAndDeletedFalse(UUID ownerId);
}
