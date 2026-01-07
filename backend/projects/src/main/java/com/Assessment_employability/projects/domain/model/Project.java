package com.Assessment_employability.projects.domain.model;

import com.Assessment_employability.projects.domain.exception.BusinessRuleException;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain Entity: Project
 * Represents a project in the system.
 * Contains business rules related to projects.
 */
public class Project {
    private UUID id;
    private UUID ownerId;
    private String name;
    private ProjectStatus status;
    private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Private constructor
    private Project() {
    }

    // Full constructor
    public Project(UUID id, UUID ownerId, String name, ProjectStatus status, boolean deleted,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.status = status;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Factory method to create a new project
    public static Project create(UUID ownerId, String name) {
        Project project = new Project();
        project.id = UUID.randomUUID();
        project.ownerId = ownerId;
        project.name = name;
        project.status = ProjectStatus.DRAFT;
        project.deleted = false;
        project.createdAt = LocalDateTime.now();
        project.updatedAt = LocalDateTime.now();
        return project;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Business rules

    /**
     * Checks if the user is the owner of the project
     */
    public boolean isOwnedBy(UUID userId) {
        return this.ownerId.equals(userId);
    }

    /**
     * Activates the project (changes from DRAFT to ACTIVE)
     * Rule: Can only be activated if in DRAFT status
     */
    public void activate() {
        if (this.status == ProjectStatus.ACTIVE) {
            throw new BusinessRuleException("Project is already active");
        }
        if (this.deleted) {
            throw new BusinessRuleException("Cannot activate a deleted project");
        }
        this.status = ProjectStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Performs a soft delete of the project
     */
    public void softDelete() {
        this.deleted = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Verifies if the project is active
     */
    public boolean isActive() {
        return this.status == ProjectStatus.ACTIVE && !this.deleted;
    }
}
