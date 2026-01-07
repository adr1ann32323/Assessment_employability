package com.Assessment_employability.projects.domain.exception;

/**
 * Domain Exception: ResourceNotFoundException
 * Thrown when a requested resource is not found.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
