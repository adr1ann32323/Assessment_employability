package com.Assessment_employability.projects.infrastructure.adapter.out.security;

import com.Assessment_employability.projects.domain.port.out.CurrentUserPort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Adapter: CurrentUserAdapter
 * Implements CurrentUserPort using Spring Security context
 */
@Component
public class CurrentUserAdapter implements CurrentUserPort {

    @Override
    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UUID) {
            return (UUID) authentication.getPrincipal();
        }
        if (authentication != null && authentication.getPrincipal() instanceof String) {
            try {
                return UUID.fromString((String) authentication.getPrincipal());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }
}

