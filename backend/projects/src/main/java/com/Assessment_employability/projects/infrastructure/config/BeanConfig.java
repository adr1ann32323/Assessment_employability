package com.Assessment_employability.projects.infrastructure.config;

import com.Assessment_employability.projects.application.service.*;
import com.Assessment_employability.projects.domain.port.in.*;
import com.Assessment_employability.projects.domain.port.out.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bean Configuration
 * Configures use case beans with their dependencies (dependency injection)
 */
@Configuration
public class BeanConfig {

    @Bean
    public CreateProjectUseCase createProjectUseCase(ProjectRepositoryPort projectRepository) {
        return new CreateProjectService(projectRepository);
    }

    @Bean
    public GetProjectsUseCase getProjectsUseCase(ProjectRepositoryPort projectRepository) {
        return new GetProjectsService(projectRepository);
    }

    @Bean
    public ActivateProjectUseCase activateProjectUseCase(
            ProjectRepositoryPort projectRepository,
            TaskRepositoryPort taskRepository,
            AuditLogPort auditLog,
            NotificationPort notification) {
        return new ActivateProjectService(projectRepository, taskRepository, auditLog, notification);
    }

    @Bean
    public CreateTaskUseCase createTaskUseCase(
            TaskRepositoryPort taskRepository,
            ProjectRepositoryPort projectRepository) {
        return new CreateTaskService(taskRepository, projectRepository);
    }

    @Bean
    public CompleteTaskUseCase completeTaskUseCase(
            TaskRepositoryPort taskRepository,
            ProjectRepositoryPort projectRepository,
            AuditLogPort auditLog,
            NotificationPort notification) {
        return new CompleteTaskService(taskRepository, projectRepository, auditLog, notification);
    }
}

