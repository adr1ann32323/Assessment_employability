package com.Assessment_employability.projects.infrastructure.adapter.out.persistence.mapper;

import com.Assessment_employability.projects.domain.model.User;
import com.Assessment_employability.projects.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between User domain model and UserEntity JPA entity
 */
@Component
public class UserMapper {

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public UserEntity toEntity(User domain) {
        if (domain == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        // Don't set ID for new entities - let JPA generate it
        // Only set ID when updating existing entities
        entity.setUsername(domain.getUsername());
        entity.setEmail(domain.getEmail());
        entity.setPassword(domain.getPassword());
        return entity;
    }

    public UserEntity toEntityWithId(User domain) {
        if (domain == null) {
            return null;
        }
        return new UserEntity(
                domain.getId(),
                domain.getUsername(),
                domain.getEmail(),
                domain.getPassword(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }
}

