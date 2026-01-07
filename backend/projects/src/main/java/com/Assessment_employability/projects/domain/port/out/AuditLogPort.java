package com.Assessment_employability.projects.domain.port.out;

import java.util.UUID;

/**
 * Output Port: AuditLogPort
 * Defines system audit operations.
 * Records critical actions performed in the system.
 */
public interface AuditLogPort {
    /**
     * Registers an audit action
     * @param action Description of the action performed
     * @param entityId ID of the affected entity
     */
    void register(String action, UUID entityId);
}
