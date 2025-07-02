package com.example.UserMicroServiceProject.domain.event;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class UserAddressRemovedEvent implements DomainEvent {

    private final String eventId;
    private final String userId;
    private final String addressId;
    private final String addressTitle;
    private final String city;
    private final boolean wasDefault;
    private final Instant eventTimestamp;

    @Override
    public String getEventType() {
        return "USER_ADDRESS_REMOVED";
    }

    @Override
    public String getAggregateId() {
        return userId;
    }

    public static UserAddressRemovedEvent create(String userId, String addressId, String addressTitle, String city, boolean wasDefault) {
        return UserAddressRemovedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .userId(userId)
                .addressId(addressId)
                .addressTitle(addressTitle)
                .city(city)
                .wasDefault(wasDefault)
                .eventTimestamp(Instant.now())
                .build();
    }
}
