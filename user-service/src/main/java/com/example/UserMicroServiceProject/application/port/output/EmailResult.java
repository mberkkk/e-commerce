package com.example.UserMicroServiceProject.application.port.output;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailResult {

    private final boolean successful;
    private final String messageId;
    private final String errorMessage;

    public static EmailResult success(String messageId) {
        return EmailResult.builder()
                .successful(true)
                .messageId(messageId)
                .build();
    }

    public static EmailResult failure(String errorMessage) {
        return EmailResult.builder()
                .successful(false)
                .errorMessage(errorMessage)
                .build();
    }
}
