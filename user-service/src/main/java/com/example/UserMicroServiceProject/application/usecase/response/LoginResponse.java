package com.example.UserMicroServiceProject.application.usecase.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Getter
@Builder
public class LoginResponse {

    private final boolean successful;
    private final String accessToken;
    private final String refreshToken;
    private final Instant expiresAt;
    private final UserResponse user;

    // Error information
    private final String errorCode;
    private final String errorMessage;

    public static LoginResponse success(String accessToken, String refreshToken,
                                        Instant expiresAt, UserResponse user) {
        return LoginResponse.builder()
                .successful(true)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresAt(expiresAt)
                .user(user)
                .build();
    }

    public static LoginResponse failure(String errorCode, String errorMessage) {
        return LoginResponse.builder()
                .successful(false)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();
    }
}
