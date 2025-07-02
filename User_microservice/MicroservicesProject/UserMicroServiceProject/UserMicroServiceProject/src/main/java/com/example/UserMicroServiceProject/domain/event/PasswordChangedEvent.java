package com.example.UserMicroServiceProject.domain.event;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class PasswordChangedEvent implements DomainEvent {

    private final String eventId;
    private final String userId;
    private final Instant eventTimestamp;

    @Override
    public String getEventType() {
        return "PASSWORD_CHANGED";
    }

    @Override
    public String getAggregateId() {
        return userId;
    }
}
