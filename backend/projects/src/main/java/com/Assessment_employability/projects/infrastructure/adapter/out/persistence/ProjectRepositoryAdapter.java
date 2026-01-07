package com.Assessment_employability.projects.infrastructure.adapter.out.persistence;

import com.Assessment_employability.projects.domain.model.Project;
import com.Assessment_employability.projects.domain.port.out.ProjectRepositoryPort;
import com.Assessment_employability.projects.infrastructure.adapter.out.persistence.entity.ProjectEntity;
import com.Assessment_employability.projects.infrastructure.adapter.out.persistence.mapper.ProjectMapper;
import com.Assessment_employability.projects.infrastructure.adapter.out.persistence.repository.JpaProjectRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter: ProjectRepositoryAdapter
 * Implements ProjectRepositoryPort using Spring Data JPA
 */
@Component
public class ProjectRepositoryAdapter implements ProjectRepositoryPort {

    private final JpaProjectRepository jpaProjectRepository;
    private final ProjectMapper projectMapper;

    public ProjectRepositoryAdapter(JpaProjectRepository jpaProjectRepository, ProjectMapper projectMapper) {
        this.jpaProjectRepository = jpaProjectRepository;
        this.projectMapper = projectMapper;
    }

    @Override
    public Project save(Project project) {
        ProjectEntity entity;
        // Check if it's a new entity or an update
        if (project.getId() != null && jpaProjectRepository.existsById(project.getId())) {
            // Updating existing entity - fetch and update
            entity = jpaProjectRepository.findById(project.getId()).orElseThrow();
            entity.setName(project.getName());
            entity.setStatus(project.getStatus().name());
            entity.setDeleted(project.isDeleted());
            entity.setUpdatedAt(project.getUpdatedAt());
        } else {
            // New entity - let JPA generate the ID
            entity = new ProjectEntity();
            entity.setOwnerId(project.getOwnerId());
            entity.setName(project.getName());
            entity.setStatus(project.getStatus().name());
            entity.setDeleted(project.isDeleted());
        }
        ProjectEntity savedEntity = jpaProjectRepository.save(entity);
        return projectMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Project> findById(UUID id) {
        return jpaProjectRepository.findByIdAndNotDeleted(id)
                .map(projectMapper::toDomain);
    }

    @Override
    public List<Project> findAllByOwnerId(UUID ownerId) {
        return jpaProjectRepository.findAllByOwnerId(ownerId)
                .stream()
                .map(projectMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Project> findAllByOwnerIdAndDeletedFalse(UUID ownerId) {
        return jpaProjectRepository.findAllByOwnerIdAndDeletedFalse(ownerId)
                .stream()
                .map(projectMapper::toDomain)
                .collect(Collectors.toList());
    }
}

