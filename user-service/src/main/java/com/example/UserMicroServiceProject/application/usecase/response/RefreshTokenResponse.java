package com.example.UserMicroServiceProject.application.usecase.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class RefreshTokenResponse {

    private final boolean successful;
    private final String newAccessToken;
    private final String newRefreshToken;
    private final Instant expiresAt;
    private final String errorMessage;

    public static RefreshTokenResponse success(String accessToken, String refreshToken, Instant expiresAt) {
        return RefreshTokenResponse.builder()
                .successful(true)
                .newAccessToken(accessToken)
                .newRefreshToken(refreshToken)
                .expiresAt(expiresAt)
                .build();
    }

    public static RefreshTokenResponse failure(String errorMessage) {
        return RefreshTokenResponse.builder()
                .successful(false)
                .errorMessage(errorMessage)
                .build();
    }
}
