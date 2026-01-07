package com.Assessment_employability.projects.domain.exception;

/**
 * Domain Exception: UnauthorizedException
 * Thrown when a user tries to access a resource that does not belong to them.
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
