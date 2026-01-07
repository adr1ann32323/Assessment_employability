package com.Assessment_employability.projects.domain.port.out;

import com.Assessment_employability.projects.domain.model.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Output Port: UserRepositoryPort
 * Defines persistence operations for users.
 * This interface belongs to the domain but will be implemented in the infrastructure layer.
 */
public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
