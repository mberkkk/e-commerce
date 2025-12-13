package com.microservices.cart_service.Event;

import lombok.Data;

import java.time.Instant;

@Data
public class UserRegisteredEvent {
    private final String eventId;
    private final String userId;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final Instant registeredAt;
    private final String registrationSource; // WEB, MOBILE, API
    private final Instant eventTimestamp;

}
