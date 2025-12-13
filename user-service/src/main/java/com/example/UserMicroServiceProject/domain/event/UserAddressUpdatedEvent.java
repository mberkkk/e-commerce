package com.example.UserMicroServiceProject.domain.event;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class UserAddressUpdatedEvent implements DomainEvent {

    private final String eventId;
    private final String userId;
    private final String addressId;
    private final String addressTitle;
    private final String city;
    private final String district;
    private final boolean isDefault;
    private final Instant eventTimestamp;

    @Override
    public String getEventType() {
        return "USER_ADDRESS_UPDATED";
    }

    @Override
    public String getAggregateId() {
        return userId;
    }

    public static UserAddressUpdatedEvent create(String userId, String addressId, String addressTitle, String city, String district, boolean isDefault) {
        return UserAddressUpdatedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .userId(userId)
                .addressId(addressId)
                .addressTitle(addressTitle)
                .city(city)
                .district(district)
                .isDefault(isDefault)
                .eventTimestamp(Instant.now())
                .build();
    }
}