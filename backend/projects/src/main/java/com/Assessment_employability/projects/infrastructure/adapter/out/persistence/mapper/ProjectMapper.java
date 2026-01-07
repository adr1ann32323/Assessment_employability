package com.Assessment_employability.projects.infrastructure.adapter.out.persistence.mapper;

import com.Assessment_employability.projects.domain.model.Project;
import com.Assessment_employability.projects.domain.model.ProjectStatus;
import com.Assessment_employability.projects.infrastructure.adapter.out.persistence.entity.ProjectEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Project domain model and ProjectEntity JPA entity
 */
@Component
public class ProjectMapper {

    public Project toDomain(ProjectEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Project(
                entity.getId(),
                entity.getOwnerId(),
                entity.getName(),
                ProjectStatus.valueOf(entity.getStatus()),
                entity.isDeleted(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public ProjectEntity toEntity(Project domain) {
        if (domain == null) {
            return null;
        }
        return new ProjectEntity(
                domain.getId(),
                domain.getOwnerId(),
                domain.getName(),
                domain.getStatus().name(),
                domain.isDeleted(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }
}

