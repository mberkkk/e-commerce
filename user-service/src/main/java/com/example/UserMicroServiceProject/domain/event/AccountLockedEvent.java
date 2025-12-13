package com.example.UserMicroServiceProject.domain.event;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class AccountLockedEvent implements DomainEvent {

    private final String eventId;
    private final String userId;
    private final String email;
    private final String lockReason;
    private final Integer failedAttempts;
    private final Instant lockedUntil;
    private final Instant eventTimestamp;

    @Override
    public String getEventType() {
        return "ACCOUNT_LOCKED";
    }

    @Override
    public String getAggregateId() {
        return userId;
    }

    public static AccountLockedEvent create(String userId, String email, String lockReason, Integer failedAttempts, Instant lockedUntil) {
        return AccountLockedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .userId(userId)
                .email(email)
                .lockReason(lockReason)
                .failedAttempts(failedAttempts)
                .lockedUntil(lockedUntil)
                .eventTimestamp(Instant.now())
                .build();
    }
}