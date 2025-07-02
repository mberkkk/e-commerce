package com.example.UserMicroServiceProject.domain.event;

import java.time.Instant;

public interface DomainEvent {

    String getEventId();
    String getEventType();
    String getAggregateId();
    Instant getEventTimestamp();

    default String getEventVersion() {
        return "1.0";
    }

    default String getEventSource() {
        return "user-service";
    }
}
