package com.Assessment_employability.projects.infrastructure.adapter.in.web.mapper;

import com.Assessment_employability.projects.domain.model.Project;
import com.Assessment_employability.projects.infrastructure.adapter.in.web.dto.ProjectResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting Project domain model to ProjectResponse DTO
 */
@Component
public class ProjectDtoMapper {

    public ProjectResponse toResponse(Project project) {
        if (project == null) {
            return null;
        }
        return new ProjectResponse(
                project.getId(),
                project.getOwnerId(),
                project.getName(),
                project.getStatus().name(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}
