package com.Assessment_employability.projects.infrastructure.adapter.in.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for task response
 */
public record TaskResponse(
        UUID id,
        UUID projectId,
        String title,
        boolean completed,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

