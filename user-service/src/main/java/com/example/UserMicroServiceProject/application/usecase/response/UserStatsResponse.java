package com.example.UserMicroServiceProject.application.usecase.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class UserStatsResponse {

    private final int trustScore;
    private final int addressCount;
    private final int failedLoginAttempts;
    private final Instant lastLoginAt;
    private final long daysSinceRegistration;
    private final boolean isHighTrustUser;
    private final String accountHealthStatus; // EXCELLENT, GOOD, NEEDS_ATTENTION, POOR
}
