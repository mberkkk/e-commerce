package com.example.UserMicroServiceProject.application.usecase.response;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LogoutResponse {

    private final boolean successful;
    private final String message;

    public static LogoutResponse success() {
        return LogoutResponse.builder()
                .successful(true)
                .message("Logout successful")
                .build();
    }

    public static LogoutResponse failure(String message) {
        return LogoutResponse.builder()
                .successful(false)
                .message(message)
                .build();
    }
}