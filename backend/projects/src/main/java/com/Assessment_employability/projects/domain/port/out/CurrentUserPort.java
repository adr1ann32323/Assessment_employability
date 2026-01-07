package com.Assessment_employability.projects.domain.port.out;

import java.util.UUID;

/**
 * Output Port: CurrentUserPort
 * Defines the operation to get the current authenticated user.
 */
public interface CurrentUserPort {
    /**
     * Gets the ID of the currently authenticated user
     * @return UUID of the current user
     */
    UUID getCurrentUserId();
}
