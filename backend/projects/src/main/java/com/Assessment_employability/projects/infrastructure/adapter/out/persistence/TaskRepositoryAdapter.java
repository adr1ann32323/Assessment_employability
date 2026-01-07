package com.Assessment_employability.projects.infrastructure.adapter.out.persistence;

import com.Assessment_employability.projects.domain.model.Task;
import com.Assessment_employability.projects.domain.port.out.TaskRepositoryPort;
import com.Assessment_employability.projects.infrastructure.adapter.out.persistence.entity.TaskEntity;
import com.Assessment_employability.projects.infrastructure.adapter.out.persistence.mapper.TaskMapper;
import com.Assessment_employability.projects.infrastructure.adapter.out.persistence.repository.JpaTaskRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter: TaskRepositoryAdapter
 * Implements TaskRepositoryPort using Spring Data JPA
 */
@Component
public class TaskRepositoryAdapter implements TaskRepositoryPort {

    private final JpaTaskRepository jpaTaskRepository;
    private final TaskMapper taskMapper;

    public TaskRepositoryAdapter(JpaTaskRepository jpaTaskRepository, TaskMapper taskMapper) {
        this.jpaTaskRepository = jpaTaskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public Task save(Task task) {
        TaskEntity entity;
        // Check if it's a new entity or an update
        if (task.getId() != null && jpaTaskRepository.existsById(task.getId())) {
            // Updating existing entity - fetch and update
            entity = jpaTaskRepository.findById(task.getId()).orElseThrow();
            entity.setTitle(task.getTitle());
            entity.setCompleted(task.isCompleted());
            entity.setDeleted(task.isDeleted());
            entity.setUpdatedAt(task.getUpdatedAt());
        } else {
            // New entity - let JPA generate the ID
            entity = new TaskEntity();
            entity.setProjectId(task.getProjectId());
            entity.setTitle(task.getTitle());
            entity.setCompleted(task.isCompleted());
            entity.setDeleted(task.isDeleted());
        }
        TaskEntity savedEntity = jpaTaskRepository.save(entity);
        return taskMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return jpaTaskRepository.findByIdAndNotDeleted(id)
                .map(taskMapper::toDomain);
    }

    @Override
    public List<Task> findAllByProjectId(UUID projectId) {
        return jpaTaskRepository.findAllByProjectId(projectId)
                .stream()
                .map(taskMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findAllByProjectIdAndDeletedFalse(UUID projectId) {
        return jpaTaskRepository.findAllByProjectIdAndDeletedFalse(projectId)
                .stream()
                .map(taskMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countActiveTasksByProjectId(UUID projectId) {
        return jpaTaskRepository.countActiveTasksByProjectId(projectId);
    }
}

