package com.example.UserMicroServiceProject.domain.event;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class UserActivatedEvent implements DomainEvent {

    private final String eventId;
    private final String userId;
    private final String activatedBy;
    private final Instant eventTimestamp;

    @Override
    public String getEventType() {
        return "USER_ACTIVATED";
    }

    @Override
    public String getAggregateId() {
        return userId;
    }
}