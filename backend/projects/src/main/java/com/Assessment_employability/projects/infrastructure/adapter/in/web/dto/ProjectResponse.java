package com.Assessment_employability.projects.infrastructure.adapter.in.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for project response
 */
public record ProjectResponse(
        UUID id,
        UUID ownerId,
        String name,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

