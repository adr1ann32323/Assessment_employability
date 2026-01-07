package com.Assessment_employability.projects.infrastructure.adapter.in.web;

import com.Assessment_employability.projects.domain.model.Task;
import com.Assessment_employability.projects.domain.port.in.CompleteTaskUseCase;
import com.Assessment_employability.projects.domain.port.in.CreateTaskUseCase;
import com.Assessment_employability.projects.domain.port.out.TaskRepositoryPort;
import com.Assessment_employability.projects.infrastructure.adapter.in.web.dto.CreateTaskRequest;
import com.Assessment_employability.projects.infrastructure.adapter.in.web.dto.TaskResponse;
import com.Assessment_employability.projects.infrastructure.adapter.in.web.mapper.TaskDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST Controller for Tasks
 * Handles task CRUD operations
 */
@RestController
@Tag(name = "Tasks", description = "Endpoints for task management")
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final CompleteTaskUseCase completeTaskUseCase;
    private final TaskRepositoryPort taskRepository;
    private final TaskDtoMapper taskDtoMapper;

    public TaskController(CreateTaskUseCase createTaskUseCase,
                          CompleteTaskUseCase completeTaskUseCase,
                          TaskRepositoryPort taskRepository,
                          TaskDtoMapper taskDtoMapper) {
        this.createTaskUseCase = createTaskUseCase;
        this.completeTaskUseCase = completeTaskUseCase;
        this.taskRepository = taskRepository;
        this.taskDtoMapper = taskDtoMapper;
    }

    @PostMapping("/api/projects/{projectId}/tasks")
    @Operation(summary = "Create a new task", description = "Creates a new task in the specified project")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable UUID projectId,
            @Valid @RequestBody CreateTaskRequest request,
            Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        Task task = createTaskUseCase.execute(projectId, request.title(), userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskDtoMapper.toResponse(task));
    }

    @GetMapping("/api/projects/{projectId}/tasks")
    @Operation(summary = "Get tasks by project", description = "Returns all tasks for a project")
    public ResponseEntity<List<TaskResponse>> getTasksByProject(@PathVariable UUID projectId) {
        List<TaskResponse> tasks = taskRepository.findAllByProjectIdAndDeletedFalse(projectId)
                .stream()
                .map(taskDtoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @PatchMapping("/api/tasks/{id}/complete")
    @Operation(summary = "Complete a task", description = "Marks a task as completed")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<TaskResponse> completeTask(
            @PathVariable UUID id,
            Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        Task task = completeTaskUseCase.execute(id, userId);
        return ResponseEntity.ok(taskDtoMapper.toResponse(task));
    }
}

