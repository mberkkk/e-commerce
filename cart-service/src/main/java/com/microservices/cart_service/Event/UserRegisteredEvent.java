package com.microservices.cart_service.Event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisteredEvent {
    private  String eventId;
    private  String userId;
    private  String email;
    private  String firstName;
    private  String lastName;
    private  String phoneNumber;
    private  Instant registeredAt;
    private  String registrationSource; // WEB, MOBILE, API
    private  Instant eventTimestamp;

}
