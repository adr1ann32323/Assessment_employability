package com.Assessment_employability.projects.infrastructure.adapter.out.persistence.repository;

import com.Assessment_employability.projects.infrastructure.adapter.out.persistence.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA Repository for ProjectEntity
 */
@Repository
public interface JpaProjectRepository extends JpaRepository<ProjectEntity, UUID> {

    List<ProjectEntity> findAllByOwnerId(UUID ownerId);

    List<ProjectEntity> findAllByOwnerIdAndDeletedFalse(UUID ownerId);

    @Query("SELECT p FROM ProjectEntity p WHERE p.id = :id AND p.deleted = false")
    Optional<ProjectEntity> findByIdAndNotDeleted(@Param("id") UUID id);
}

