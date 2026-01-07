package com.Assessment_employability.projects.domain.model;

import com.Assessment_employability.projects.domain.exception.BusinessRuleException;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain Entity: Task
 * Represents a task within a project.
 * Contains business rules related to tasks.
 */
public class Task {
    private UUID id;
    private UUID projectId;
    private String title;
    private boolean completed;
    private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Private constructor
    private Task() {
    }

    // Full constructor
    public Task(UUID id, UUID projectId, String title, boolean completed, boolean deleted,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.completed = completed;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Factory method to create a new task
    public static Task create(UUID projectId, String title) {
        Task task = new Task();
        task.id = UUID.randomUUID();
        task.projectId = projectId;
        task.title = title;
        task.completed = false;
        task.deleted = false;
        task.createdAt = LocalDateTime.now();
        task.updatedAt = LocalDateTime.now();
        return task;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
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
     * Marks the task as completed
     * Rule: A completed task cannot be modified again
     */
    public void complete() {
        if (this.completed) {
            throw new BusinessRuleException("Task is already completed");
        }
        if (this.deleted) {
            throw new BusinessRuleException("Cannot complete a deleted task");
        }
        this.completed = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Performs a soft delete of the task
     */
    public void softDelete() {
        this.deleted = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Verifies if the task is active (not deleted and not completed)
     */
    public boolean isActive() {
        return !this.deleted && !this.completed;
    }
}
