package com.example.UserMicroServiceProject.domain.event;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class UserAddressAddedEvent implements DomainEvent {

    private final String eventId;
    private final String userId;
    private final String addressId;
    private final String addressTitle;
    private final String city;
    private final String district;
    private final boolean isDefault;
    private final boolean isBusinessAddress;
    private final Instant eventTimestamp;

    @Override
    public String getEventType() {
        return "USER_ADDRESS_ADDED";
    }

    @Override
    public String getAggregateId() {
        return userId;
    }
}
