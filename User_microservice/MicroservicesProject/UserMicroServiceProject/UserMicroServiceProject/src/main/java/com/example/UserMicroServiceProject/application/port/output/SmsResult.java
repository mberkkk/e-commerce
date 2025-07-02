package com.example.UserMicroServiceProject.application.port.output;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SmsResult {

    private final boolean successful;
    private final String messageId;
    private final String errorMessage;

    public static SmsResult success(String messageId) {
        return SmsResult.builder()
                .successful(true)
                .messageId(messageId)
                .build();
    }

    public static SmsResult failure(String errorMessage) {
        return SmsResult.builder()
                .successful(false)
                .errorMessage(errorMessage)
                .build();
    }
}
