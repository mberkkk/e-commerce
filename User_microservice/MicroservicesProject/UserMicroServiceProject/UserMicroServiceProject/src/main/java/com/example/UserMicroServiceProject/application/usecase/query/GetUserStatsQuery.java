package com.example.UserMicroServiceProject.application.usecase.query;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetUserStatsQuery {

    @NotBlank(message = "User ID cannot be blank")
    private final String userId;

    private final boolean includeTrustScore;
    private final boolean includeLoginHistory;
    private final boolean includeAddressStats;
}
