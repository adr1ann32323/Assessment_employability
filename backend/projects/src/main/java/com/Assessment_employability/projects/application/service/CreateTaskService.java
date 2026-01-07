package com.Assessment_employability.projects.application.service;

import com.Assessment_employability.projects.domain.exception.ResourceNotFoundException;
import com.Assessment_employability.projects.domain.exception.UnauthorizedException;
import com.Assessment_employability.projects.domain.model.Project;
import com.Assessment_employability.projects.domain.model.Task;
import com.Assessment_employability.projects.domain.port.in.CreateTaskUseCase;
import com.Assessment_employability.projects.domain.port.out.ProjectRepositoryPort;
import com.Assessment_employability.projects.domain.port.out.TaskRepositoryPort;

import java.util.UUID;

/**
 * Use Case: Create Task
 * Implements the logic to create a task in a project.
 *
 * Business rules:
 * 1. Only the project owner can create tasks
 * 2. The project must exist and not be deleted
 */
public class CreateTaskService implements CreateTaskUseCase {

    private final TaskRepositoryPort taskRepository;
    private final ProjectRepositoryPort projectRepository;

    public CreateTaskService(TaskRepositoryPort taskRepository, ProjectRepositoryPort projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public Task execute(UUID projectId, String title, UUID userId) {
        // 1. Verify project exists
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        // 2. Verify user is the project owner
        if (!project.isOwnedBy(userId)) {
            throw new UnauthorizedException("You do not have permission to create tasks in this project");
        }

        // 3. Create the task
        Task task = Task.create(projectId, title);

        // 4. Persist the task
        return taskRepository.save(task);
    }
}
