package com.Assessment_employability.projects.infrastructure.adapter.out.notification;

import com.Assessment_employability.projects.domain.port.out.NotificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Adapter: LogNotificationAdapter
 * Implements NotificationPort using console logging
 * This is a simple implementation that can be replaced with email, SMS, etc.
 */
@Component
public class LogNotificationAdapter implements NotificationPort {

    private static final Logger logger = LoggerFactory.getLogger(LogNotificationAdapter.class);

    @Override
    public void notify(String message) {
        logger.info("NOTIFICATION: {}", message);
    }
}

