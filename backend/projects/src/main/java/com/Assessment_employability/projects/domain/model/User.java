package com.Assessment_employability.projects.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain Entity: User
 * Represents a user in the system.
 * This class is part of the domain core and does not depend on frameworks.
 */
public class User {
    private UUID id;
    private String username;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Private constructor to force usage of factory method
    private User() {
    }

    // Full constructor
    public User(UUID id, String username, String email, String password,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Factory method to create a new user
    public static User create(String username, String email, String password) {
        User user = new User();
        user.id = UUID.randomUUID();
        user.username = username;
        user.email = email;
        user.password = password;
        user.createdAt = LocalDateTime.now();
        user.updatedAt = LocalDateTime.now();
        return user;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Business methods
    public void updatePassword(String newPassword) {
        this.password = newPassword;
        this.updatedAt = LocalDateTime.now();
    }
}
