package com.Assessment_employability.projects.infrastructure.adapter.out.persistence.repository;

import com.Assessment_employability.projects.infrastructure.adapter.out.persistence.entity.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA Repository for AuditLogEntity
 */
@Repository
public interface JpaAuditLogRepository extends JpaRepository<AuditLogEntity, UUID> {
}

