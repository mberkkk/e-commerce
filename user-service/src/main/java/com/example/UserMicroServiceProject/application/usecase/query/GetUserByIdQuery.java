package com.example.UserMicroServiceProject.application.usecase.query;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class GetUserByIdQuery {

    @NotBlank(message = "User ID cannot be blank")
    private final String userId;

    private final boolean includeAddresses; // Include addresses in response
}
