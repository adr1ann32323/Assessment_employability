package com.Assessment_employability.projects.application.service;

import com.Assessment_employability.projects.domain.exception.BusinessRuleException;
import com.Assessment_employability.projects.domain.exception.ResourceNotFoundException;
import com.Assessment_employability.projects.domain.exception.UnauthorizedException;
import com.Assessment_employability.projects.domain.model.Project;
import com.Assessment_employability.projects.domain.model.ProjectStatus;
import com.Assessment_employability.projects.domain.model.Task;
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
 * Unit tests for CompleteTaskService
 * Tests business rules for task completion
 */
@ExtendWith(MockitoExtension.class)
class CompleteTaskServiceTest {

    @Mock
    private TaskRepositoryPort taskRepository;

    @Mock
    private ProjectRepositoryPort projectRepository;

    @Mock
    private AuditLogPort auditLog;

    @Mock
    private NotificationPort notification;

    private CompleteTaskService completeTaskService;

    private UUID taskId;
    private UUID projectId;
    private UUID ownerId;
    private UUID otherUserId;

    @BeforeEach
    void setUp() {
        completeTaskService = new CompleteTaskService(
                taskRepository, projectRepository, auditLog, notification);
        taskId = UUID.randomUUID();
        projectId = UUID.randomUUID();
        ownerId = UUID.randomUUID();
        otherUserId = UUID.randomUUID();
    }

    @Test
    @DisplayName("CompleteTask_ShouldGenerateAuditAndNotification")
    void completeTask_ShouldGenerateAuditAndNotification() {
        // Arrange
        Task task = new Task(
                taskId, projectId, "Test Task", false, false,
                LocalDateTime.now(), LocalDateTime.now());
        Project project = new Project(
                projectId, ownerId, "Test Project", ProjectStatus.ACTIVE, false,
                LocalDateTime.now(), LocalDateTime.now());

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Task result = completeTaskService.execute(taskId, ownerId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isCompleted());
        verify(auditLog).register("TASK_COMPLETED", taskId);
        verify(notification).notify(contains("has been completed"));
    }

    @Test
    @DisplayName("CompleteTask_AlreadyCompleted_ShouldFail")
    void completeTask_AlreadyCompleted_ShouldFail() {
        // Arrange
        Task task = new Task(
                taskId, projectId, "Test Task", true, false,
                LocalDateTime.now(), LocalDateTime.now());
        Project project = new Project(
                projectId, ownerId, "Test Project", ProjectStatus.ACTIVE, false,
                LocalDateTime.now(), LocalDateTime.now());

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // Act & Assert
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> completeTaskService.execute(taskId, ownerId)
        );

        assertEquals("Task is already completed", exception.getMessage());
        verify(auditLog, never()).register(any(), any());
        verify(notification, never()).notify(any());
    }

    @Test
    @DisplayName("CompleteTask_ByNonOwner_ShouldFail")
    void completeTask_ByNonOwner_ShouldFail() {
        // Arrange
        Task task = new Task(
                taskId, projectId, "Test Task", false, false,
                LocalDateTime.now(), LocalDateTime.now());
        Project project = new Project(
                projectId, ownerId, "Test Project", ProjectStatus.ACTIVE, false,
                LocalDateTime.now(), LocalDateTime.now());

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // Act & Assert
        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> completeTaskService.execute(taskId, otherUserId)
        );

        assertEquals("You do not have permission to complete this task", exception.getMessage());
        verify(auditLog, never()).register(any(), any());
    }

    @Test
    @DisplayName("CompleteTask_NotFound_ShouldFail")
    void completeTask_NotFound_ShouldFail() {
        // Arrange
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> completeTaskService.execute(taskId, ownerId)
        );

        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    @DisplayName("CompleteTask_DeletedTask_ShouldFail")
    void completeTask_DeletedTask_ShouldFail() {
        // Arrange
        Task task = new Task(
                taskId, projectId, "Test Task", false, true,
                LocalDateTime.now(), LocalDateTime.now());
        Project project = new Project(
                projectId, ownerId, "Test Project", ProjectStatus.ACTIVE, false,
                LocalDateTime.now(), LocalDateTime.now());

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // Act & Assert
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> completeTaskService.execute(taskId, ownerId)
        );

        assertEquals("Cannot complete a deleted task", exception.getMessage());
    }
}

