package com.Assessment_employability.projects.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for project creation request
 */
public record CreateProjectRequest(
        @NotBlank(message = "Project name is required")
        @Size(min = 1, max = 100, message = "Project name must be between 1 and 100 characters")
        String name
) {}
