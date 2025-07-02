package org.example.notification.service;

import org.example.notification.dto.NotificationRequest;
import org.example.notification.dto.NotificationResponse;
import java.util.List;

public interface NotificationService {
    List<NotificationResponse> getAllNotifications();
    NotificationResponse createNotification(NotificationRequest request);
    NotificationResponse getNotification(Long id);
    List<NotificationResponse> getNotificationsByUserId(String userId);
    List<NotificationResponse> getNotificationsByStatus(String status);
    NotificationResponse updateNotification(Long id, NotificationRequest request);
    void deleteNotification(Long id);
} 