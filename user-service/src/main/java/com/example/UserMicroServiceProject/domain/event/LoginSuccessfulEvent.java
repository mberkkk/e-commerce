package com.example.UserMicroServiceProject.domain.event;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class LoginSuccessfulEvent implements DomainEvent {

    private final String eventId;
    private final String userId;
    private final String email;
    private final String ipAddress;
    private final String userAgent;
    private final Instant loginTime;
    private final Instant eventTimestamp;

    @Override
    public String getEventType() {
        return "LOGIN_SUCCESSFUL";
    }

    @Override
    public String getAggregateId() {
        return userId;
    }

    public static LoginSuccessfulEvent create(String userId, String email, String ipAddress, String userAgent) {
        return LoginSuccessfulEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .userId(userId)
                .email(email)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .loginTime(Instant.now())
                .eventTimestamp(Instant.now())
                .build();
    }
}
