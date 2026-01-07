package com.Assessment_employability.projects.application.service;

import com.Assessment_employability.projects.domain.exception.BusinessRuleException;
import com.Assessment_employability.projects.domain.exception.ResourceNotFoundException;
import com.Assessment_employability.projects.domain.exception.UnauthorizedException;
import com.Assessment_employability.projects.domain.model.Project;
import com.Assessment_employability.projects.domain.port.in.ActivateProjectUseCase;
import com.Assessment_employability.projects.domain.port.out.AuditLogPort;
import com.Assessment_employability.projects.domain.port.out.NotificationPort;
import com.Assessment_employability.projects.domain.port.out.ProjectRepositoryPort;
import com.Assessment_employability.projects.domain.port.out.TaskRepositoryPort;

import java.util.UUID;

/**
 * Use Case: Activate Project
 * Implements the logic to activate a project.
 *
 * Business rules:
 * 1. Only the owner can activate the project
 * 2. The project must have at least one active task
 * 3. Audit and notification are generated
 */
public class ActivateProjectService implements ActivateProjectUseCase {

    private final ProjectRepositoryPort projectRepository;
    private final TaskRepositoryPort taskRepository;
    private final AuditLogPort auditLog;
    private final NotificationPort notification;

    public ActivateProjectService(
            ProjectRepositoryPort projectRepository,
            TaskRepositoryPort taskRepository,
            AuditLogPort auditLog,
            NotificationPort notification) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.auditLog = auditLog;
        this.notification = notification;
    }

    @Override
    public Project execute(UUID projectId, UUID userId) {
        // 1. Find the project
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        // 2. Verify user is the owner
        if (!project.isOwnedBy(userId)) {
            throw new UnauthorizedException("You do not have permission to activate this project");
        }

        // 3. Verify project has at least one active task
        long activeTasks = taskRepository.countActiveTasksByProjectId(projectId);
        if (activeTasks == 0) {
            throw new BusinessRuleException("Project must have at least one task to be activated");
        }

        // 4. Activate the project (state validation is in the domain)
        project.activate();

        // 5. Persist the change
        Project savedProject = projectRepository.save(project);

        // 6. Register audit
        auditLog.register("PROJECT_ACTIVATED", projectId);

        // 7. Send notification
        notification.notify("Project '" + project.getName() + "' has been activated");

        return savedProject;
    }
}
