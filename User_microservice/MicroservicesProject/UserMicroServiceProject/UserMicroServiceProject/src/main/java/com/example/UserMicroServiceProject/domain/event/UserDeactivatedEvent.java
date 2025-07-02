package com.example.UserMicroServiceProject.domain.event;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class UserDeactivatedEvent implements DomainEvent {

    private final String eventId;
    private final String userId;
    private final String reason;
    private final String deactivatedBy;
    private final Instant eventTimestamp;

    @Override
    public String getEventType() {
        return "USER_DEACTIVATED";
    }

    @Override
    public String getAggregateId() {
        return userId;
    }
}