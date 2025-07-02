package org.example.notification.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.notification.dto.NotificationRequest;
import org.example.notification.dto.NotificationResponse;
import org.example.notification.exception.NotificationNotFoundException;
import org.example.notification.mapper.NotificationMapper;
import org.example.notification.model.Notification;
import org.example.notification.repository.NotificationRepository;
import org.example.notification.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NotificationResponse createNotification(NotificationRequest request) {
        Notification notification = notificationMapper.toEntity(request);
        notification = notificationRepository.save(notification);
        return notificationMapper.toResponse(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponse getNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id: " + id));
        return notificationMapper.toResponse(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsByUserId(String userId) {
        return notificationRepository.findByUserId(userId).stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsByStatus(String status) {
        return notificationRepository.findByStatus(status).stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NotificationResponse updateNotification(Long id, NotificationRequest request) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id: " + id));
        notificationMapper.updateEntity(notification, request);
        notification = notificationRepository.save(notification);
        return notificationMapper.toResponse(notification);
    }

    @Override
    @Transactional
    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new NotificationNotFoundException("Notification not found with id: " + id);
        }
        notificationRepository.deleteById(id);
    }
} 