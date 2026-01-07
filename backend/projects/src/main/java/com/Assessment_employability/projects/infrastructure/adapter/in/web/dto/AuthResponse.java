package com.Assessment_employability.projects.infrastructure.adapter.in.web.dto;

import java.util.UUID;

/**
 * DTO for authentication response containing JWT token
 */
public record AuthResponse(
        String token,
        UUID userId,
        String username,
        String message
) {}

