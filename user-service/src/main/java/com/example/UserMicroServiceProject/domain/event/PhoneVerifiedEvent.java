package com.example.UserMicroServiceProject.domain.event;
import lombok.Builder;
import lombok.Getter;
import java.time.Instant;

@Getter
@Builder
public class PhoneVerifiedEvent implements DomainEvent {

    private final String eventId;
    private final String userId;
    private final String phoneNumber;
    private final Instant verifiedAt;
    private final Instant eventTimestamp;

    @Override
    public String getEventType() {
        return "PHONE_VERIFIED";
    }

    @Override
    public String getAggregateId() {
        return userId;
    }

    public static PhoneVerifiedEvent create(String userId, String phoneNumber) {
        return PhoneVerifiedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .userId(userId)
                .phoneNumber(phoneNumber)
                .verifiedAt(Instant.now())
                .eventTimestamp(Instant.now())
                .build();
    }
}
