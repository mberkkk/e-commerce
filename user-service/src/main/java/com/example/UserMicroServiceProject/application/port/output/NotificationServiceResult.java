package com.example.UserMicroServiceProject.application.port.output;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class NotificationServiceResult {

    private final boolean successful;
    private final Map<String, Boolean> preferences; // notification type -> enabled
    private final String errorMessage;

    public static NotificationServiceResult success(Map<String, Boolean> preferences) {
        return NotificationServiceResult.builder()
                .successful(true)
                .preferences(preferences)
                .build();
    }

    public static NotificationServiceResult failure(String errorMessage) {
        return NotificationServiceResult.builder()
                .successful(false)
                .errorMessage(errorMessage)
                .build();
    }
}
