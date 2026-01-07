package com.Assessment_employability.projects.application.service;

import com.Assessment_employability.projects.domain.exception.ResourceNotFoundException;
import com.Assessment_employability.projects.domain.exception.UnauthorizedException;
import com.Assessment_employability.projects.domain.model.Project;
import com.Assessment_employability.projects.domain.model.Task;
import com.Assessment_employability.projects.domain.port.in.CompleteTaskUseCase;
import com.Assessment_employability.projects.domain.port.out.AuditLogPort;
import com.Assessment_employability.projects.domain.port.out.NotificationPort;
import com.Assessment_employability.projects.domain.port.out.ProjectRepositoryPort;
import com.Assessment_employability.projects.domain.port.out.TaskRepositoryPort;

import java.util.UUID;

/**
 * Use Case: Complete Task
 * Implements the logic to complete a task.
 *
 * Business rules:
 * 1. Only the project owner can complete tasks
 * 2. A completed task cannot be modified again
 * 3. Audit and notification are generated
 */
public class CompleteTaskService implements CompleteTaskUseCase {

    private final TaskRepositoryPort taskRepository;
    private final ProjectRepositoryPort projectRepository;
    private final AuditLogPort auditLog;
    private final NotificationPort notification;

    public CompleteTaskService(
            TaskRepositoryPort taskRepository,
            ProjectRepositoryPort projectRepository,
            AuditLogPort auditLog,
            NotificationPort notification) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.auditLog = auditLog;
        this.notification = notification;
    }

    @Override
    public Task execute(UUID taskId, UUID userId) {
        // 1. Find the task
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        // 2. Find the project to verify ownership
        Project project = projectRepository.findById(task.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        // 3. Verify user is the project owner
        if (!project.isOwnedBy(userId)) {
            throw new UnauthorizedException("You do not have permission to complete this task");
        }

        // 4. Complete the task (state validation is in the domain)
        task.complete();

        // 5. Persist the change
        Task savedTask = taskRepository.save(task);

        // 6. Register audit
        auditLog.register("TASK_COMPLETED", taskId);

        // 7. Send notification
        notification.notify("Task '" + task.getTitle() + "' has been completed");

        return savedTask;
    }
}
