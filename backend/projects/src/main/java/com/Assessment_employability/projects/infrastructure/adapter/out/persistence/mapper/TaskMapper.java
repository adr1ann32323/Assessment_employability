package com.Assessment_employability.projects.infrastructure.adapter.out.persistence.mapper;

import com.Assessment_employability.projects.domain.model.Task;
import com.Assessment_employability.projects.infrastructure.adapter.out.persistence.entity.TaskEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Task domain model and TaskEntity JPA entity
 */
@Component
public class TaskMapper {

    public Task toDomain(TaskEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Task(
                entity.getId(),
                entity.getProjectId(),
                entity.getTitle(),
                entity.isCompleted(),
                entity.isDeleted(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public TaskEntity toEntity(Task domain) {
        if (domain == null) {
            return null;
        }
        return new TaskEntity(
                domain.getId(),
                domain.getProjectId(),
                domain.getTitle(),
                domain.isCompleted(),
                domain.isDeleted(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }
}

