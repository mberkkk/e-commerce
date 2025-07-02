package com.example.UserMicroServiceProject.domain.event;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class LoginFailedEvent implements DomainEvent {

    private final String eventId;
    private final String email;
    private final String failureReason;
    private final String ipAddress;
    private final String userAgent;
    private final Integer attemptCount;
    private final Instant eventTimestamp;

    @Override
    public String getEventType() {
        return "LOGIN_FAILED";
    }

    @Override
    public String getAggregateId() {
        return email; // User might not exist, so use email
    }

    public static LoginFailedEvent create(String email, String failureReason, String ipAddress, String userAgent, Integer attemptCount) {
        return LoginFailedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .email(email)
                .failureReason(failureReason)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .attemptCount(attemptCount)
                .eventTimestamp(Instant.now())
                .build();
    }
}