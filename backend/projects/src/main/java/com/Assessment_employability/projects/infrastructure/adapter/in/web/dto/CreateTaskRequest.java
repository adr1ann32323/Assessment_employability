package com.Assessment_employability.projects.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for task creation request
 */
public record CreateTaskRequest(
        @NotBlank(message = "Task title is required")
        @Size(min = 1, max = 200, message = "Task title must be between 1 and 200 characters")
        String title
) {}

