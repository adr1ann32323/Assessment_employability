package com.Assessment_employability.projects.domain.port.out;

/**
 * Output Port: NotificationPort
 * Defines system notification operations.
 * This port allows sending notifications of important events.
 */
public interface NotificationPort {
    /**
     * Sends a notification
     * @param message Message to notify
     */
    void notify(String message);
}
