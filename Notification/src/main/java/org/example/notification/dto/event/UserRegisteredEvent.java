package org.example.notification.dto.event;

import lombok.Data;

@Data
public class UserRegisteredEvent {
    private String userId;
    private String email;
    private String username;
} 