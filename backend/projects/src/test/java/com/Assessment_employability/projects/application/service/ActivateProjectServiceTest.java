package com.Assessment_employability.projects.application.service;

import com.Assessment_employability.projects.domain.exception.BusinessRuleException;
import com.Assessment_employability.projects.domain.exception.ResourceNotFoundException;
import com.Assessment_employability.projects.domain.exception.UnauthorizedException;
import com.Assessment_employability.projects.domain.model.Project;
import com.Assessment_employability.projects.domain.model.ProjectStatus;
import com.Assessment_employability.projects.domain.port.out.AuditLogPort;
import com.Assessment_employability.projects.domain.port.out.NotificationPort;
import com.Assessment_employability.projects.domain.port.out.ProjectRepositoryPort;
import com.Assessment_employability.projects.domain.port.out.TaskRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ActivateProjectService
 * Tests business rules for project activation
 */
@ExtendWith(MockitoExtension.class)
class ActivateProjectServiceTest {

    @Mock
    private ProjectRepositoryPort projectRepository;

    @Mock
    private TaskRepositoryPort taskRepository;

    @Mock
    private AuditLogPort auditLog;

    @Mock
    private NotificationPort notification;

    private ActivateProjectService activateProjectService;

    private UUID projectId;
    private UUID ownerId;
    private UUID otherUserId;

    @BeforeEach
    void setUp() {
        activateProjectService = new ActivateProjectService(
                projectRepository, taskRepository, auditLog, notification);
        projectId = UUID.randomUUID();
        ownerId = UUID.randomUUID();
        otherUserId = UUID.randomUUID();
    }

    @Test
    @DisplayName("ActivateProject_WithTasks_ShouldSucceed")
    void activateProject_WithTasks_ShouldSucceed() {
        // Arrange
        Project project = new Project(
                projectId, ownerId, "Test Project", ProjectStatus.DRAFT, false,
                LocalDateTime.now(), LocalDateTime.now());

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskRepository.countActiveTasksByProjectId(projectId)).thenReturn(2L);
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Project result = activateProjectService.execute(projectId, ownerId);

        // Assert
        assertNotNull(result);
        assertEquals(ProjectStatus.ACTIVE, result.getStatus());
        verify(auditLog).register("PROJECT_ACTIVATED", projectId);
        verify(notification).notify(contains("has been activated"));
    }

    @Test
    @DisplayName("ActivateProject_WithoutTasks_ShouldFail")
    void activateProject_WithoutTasks_ShouldFail() {
        // Arrange
        Project project = new Project(
                projectId, ownerId, "Test Project", ProjectStatus.DRAFT, false,
                LocalDateTime.now(), LocalDateTime.now());

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskRepository.countActiveTasksByProjectId(projectId)).thenReturn(0L);

        // Act & Assert
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> activateProjectService.execute(projectId, ownerId)
        );

        assertEquals("Project must have at least one task to be activated", exception.getMessage());
        verify(auditLog, never()).register(any(), any());
        verify(notification, never()).notify(any());
    }

    @Test
    @DisplayName("ActivateProject_ByNonOwner_ShouldFail")
    void activateProject_ByNonOwner_ShouldFail() {
        // Arrange
        Project project = new Project(
                projectId, ownerId, "Test Project", ProjectStatus.DRAFT, false,
                LocalDateTime.now(), LocalDateTime.now());

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // Act & Assert
        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> activateProjectService.execute(projectId, otherUserId)
        );

        assertEquals("You do not have permission to activate this project", exception.getMessage());
        verify(taskRepository, never()).countActiveTasksByProjectId(any());
        verify(auditLog, never()).register(any(), any());
    }

    @Test
    @DisplayName("ActivateProject_NotFound_ShouldFail")
    void activateProject_NotFound_ShouldFail() {
        // Arrange
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> activateProjectService.execute(projectId, ownerId)
        );

        assertEquals("Project not found", exception.getMessage());
    }

    @Test
    @DisplayName("ActivateProject_AlreadyActive_ShouldFail")
    void activateProject_AlreadyActive_ShouldFail() {
        // Arrange
        Project project = new Project(
                projectId, ownerId, "Test Project", ProjectStatus.ACTIVE, false,
                LocalDateTime.now(), LocalDateTime.now());

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskRepository.countActiveTasksByProjectId(projectId)).thenReturn(1L);

        // Act & Assert
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> activateProjectService.execute(projectId, ownerId)
        );

        assertEquals("Project is already active", exception.getMessage());
    }
}

