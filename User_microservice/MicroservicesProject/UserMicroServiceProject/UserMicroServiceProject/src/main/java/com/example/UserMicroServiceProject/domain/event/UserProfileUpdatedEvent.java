package com.example.UserMicroServiceProject.domain.event;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
@Builder
public class UserProfileUpdatedEvent implements DomainEvent {

    private final String eventId;
    private final String userId;
    private final Map<String, Object> changedFields;
    private final String updatedBy;
    private final Instant eventTimestamp;

    @Override
    public String getEventType() {
        return "USER_PROFILE_UPDATED";
    }

    @Override
    public String getAggregateId() {
        return userId;
    }
}
