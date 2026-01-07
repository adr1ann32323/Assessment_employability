package com.Assessment_employability.projects.infrastructure.adapter.out.audit;

import com.Assessment_employability.projects.domain.port.out.AuditLogPort;
import com.Assessment_employability.projects.infrastructure.adapter.out.persistence.entity.AuditLogEntity;
import com.Assessment_employability.projects.infrastructure.adapter.out.persistence.repository.JpaAuditLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Adapter: AuditLogAdapter
 * Implements AuditLogPort for logging audit events to database
 */
@Component
public class AuditLogAdapter implements AuditLogPort {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogAdapter.class);
    private final JpaAuditLogRepository auditLogRepository;

    public AuditLogAdapter(JpaAuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public void register(String action, UUID entityId) {
        UUID userId = getCurrentUserId();

        AuditLogEntity auditLog = new AuditLogEntity();
        auditLog.setAction(action);
        auditLog.setEntityId(entityId);
        auditLog.setUserId(userId);

        auditLogRepository.save(auditLog);
        logger.info("Audit: {} - Entity: {} - User: {}", action, entityId, userId);
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UUID) {
            return (UUID) authentication.getPrincipal();
        }
        return null;
    }
}

