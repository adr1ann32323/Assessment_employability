package com.Assessment_employability.projects.infrastructure.adapter.out.persistence.repository;

import com.Assessment_employability.projects.infrastructure.adapter.out.persistence.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA Repository for TaskEntity
 */
@Repository
public interface JpaTaskRepository extends JpaRepository<TaskEntity, UUID> {

    List<TaskEntity> findAllByProjectId(UUID projectId);

    List<TaskEntity> findAllByProjectIdAndDeletedFalse(UUID projectId);

    @Query("SELECT t FROM TaskEntity t WHERE t.id = :id AND t.deleted = false")
    Optional<TaskEntity> findByIdAndNotDeleted(@Param("id") UUID id);

    @Query("SELECT COUNT(t) FROM TaskEntity t WHERE t.projectId = :projectId AND t.deleted = false")
    long countActiveTasksByProjectId(@Param("projectId") UUID projectId);
}

