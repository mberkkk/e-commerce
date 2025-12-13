package com.example.UserMicroServiceProject.domain.event;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class UserDeletedEvent implements DomainEvent {

    private final String eventId;
    private final String userId;
    private final String email;
    private final String deletionReason;
    private final String deletedBy;
    private final Instant eventTimestamp;

    @Override
    public String getEventType() {
        return "USER_DELETED";
    }

    @Override
    public String getAggregateId() {
        return userId;
    }

    public static UserDeletedEvent create(String userId, String email, String deletionReason, String deletedBy) {
        return UserDeletedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .userId(userId)
                .email(email)
                .deletionReason(deletionReason)
                .deletedBy(deletedBy)
                .eventTimestamp(Instant.now())
                .build();
    }
}
