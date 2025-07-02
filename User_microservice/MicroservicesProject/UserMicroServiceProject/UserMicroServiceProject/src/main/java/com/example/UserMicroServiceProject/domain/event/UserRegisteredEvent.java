package com.example.UserMicroServiceProject.domain.event;

import com.example.UserMicroServiceProject.domain.model.user.User;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class UserRegisteredEvent implements DomainEvent {

    private final String eventId;
    private final String userId;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final Instant registeredAt;
    private final String registrationSource; // WEB, MOBILE, API
    private final Instant eventTimestamp;

    public static UserRegisteredEvent from(User user) {
        return UserRegisteredEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .userId(user.getId().getId())
                .email(user.getEmail().getValue())
                .firstName(user.getName().getFirstName())
                .lastName(user.getName().getLastName())
                .phoneNumber(user.getPhoneNumber() != null ? user.getPhoneNumber().getValue() : null)
                .registeredAt(user.getCreatedAt())
                .registrationSource("WEB") // Default
                .eventTimestamp(Instant.now())
                .build();
    }

    @Override
    public String getEventType() {
        return "USER_REGISTERED";
    }

    @Override
    public String getAggregateId() {
        return userId;
    }
}
