package com.example.UserMicroServiceProject.domain.event;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class EmailVerifiedEvent implements DomainEvent {

    private final String eventId;
    private final String userId;
    private final String email;
    private final Instant verifiedAt;
    private final Instant eventTimestamp;

    @Override
    public String getEventType() {
        return "EMAIL_VERIFIED";
    }

    @Override
    public String getAggregateId() {
        return userId;
    }

    public static EmailVerifiedEvent create(String userId, String email) {
        return EmailVerifiedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .userId(userId)
                .email(email)
                .verifiedAt(Instant.now())
                .eventTimestamp(Instant.now())
                .build();
    }
}