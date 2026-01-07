package com.Assessment_employability.projects.domain.exception;

/**
 * Domain Exception: BusinessRuleException
 * Thrown when a business rule is violated.
 */
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
