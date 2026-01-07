package com.Assessment_employability.projects.infrastructure.adapter.in.web.mapper;

import com.Assessment_employability.projects.domain.model.Task;
import com.Assessment_employability.projects.infrastructure.adapter.in.web.dto.TaskResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting Task domain model to TaskResponse DTO
 */
@Component
public class TaskDtoMapper {

    public TaskResponse toResponse(Task task) {
        if (task == null) {
            return null;
        }
        return new TaskResponse(
                task.getId(),
                task.getProjectId(),
                task.getTitle(),
                task.isCompleted(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}

