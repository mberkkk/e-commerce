package com.example.UserMicroServiceProject.infrastructure.messaging;

import com.example.UserMicroServiceProject.application.port.output.EventPublisherPort;
import com.example.UserMicroServiceProject.domain.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaEventPublisherAdapter implements EventPublisherPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishUserRegistered(UserRegisteredEvent event) {
        NotificationUserRegisteredEvent notificationEvent = new NotificationUserRegisteredEvent();
        notificationEvent.setUserId(event.getUserId());
        notificationEvent.setEmail(event.getEmail());
        notificationEvent.setUsername(event.getFirstName() + " " + event.getLastName());
        kafkaTemplate.send("user-registered", event.getUserId(), notificationEvent);
        log.info("Published user-registered event for user: {}", event.getUserId());
    }

    @Override
    public void publishUserActivated(UserActivatedEvent event) {
        kafkaTemplate.send("USER_ACTIVATED", event.getUserId(), event);
        log.info("Published USER_ACTIVATED event for user: {}", event.getUserId());
    }

    @Override
    public void publishUserDeactivated(UserDeactivatedEvent event) {
        kafkaTemplate.send("USER_DEACTIVATED", event.getUserId(), event);
        log.info("Published USER_DEACTIVATED event for user: {}", event.getUserId());
    }

    @Override
    public void publishProfileUpdated(UserProfileUpdatedEvent event) {
        kafkaTemplate.send("USER_PROFILE_UPDATED", event.getUserId(), event);
        log.info("Published USER_PROFILE_UPDATED event for user: {}", event.getUserId());
    }

    @Override
    public void publishPasswordChanged(PasswordChangedEvent event) {
        kafkaTemplate.send("PASSWORD_CHANGED", event.getUserId(), event);
        log.info("Published PASSWORD_CHANGED event for user: {}", event.getUserId());
    }

    @Override
    public void publishDomainEvent(DomainEvent event) {
        kafkaTemplate.send(event.getEventType(), event.getAggregateId(), event);
        log.info("Published {} event for aggregate: {}", event.getEventType(), event.getAggregateId());
    }

    public static class NotificationUserRegisteredEvent {
        private String userId;
        private String email;
        private String username;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}