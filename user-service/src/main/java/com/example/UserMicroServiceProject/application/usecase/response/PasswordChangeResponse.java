package com.example.UserMicroServiceProject.application.usecase.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordChangeResponse {

    private final boolean successful;
    private final String message;
    private final boolean requiresReauthentication;

    public static PasswordChangeResponse success() {
        return PasswordChangeResponse.builder()
                .successful(true)
                .message("Password changed successfully")
                .requiresReauthentication(true)
                .build();
    }

    public static PasswordChangeResponse failure(String message) {
        return PasswordChangeResponse.builder()
                .successful(false)
                .message(message)
                .requiresReauthentication(false)
                .build();
    }
}
