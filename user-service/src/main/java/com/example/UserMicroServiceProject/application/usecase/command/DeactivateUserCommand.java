package com.example.UserMicroServiceProject.application.usecase.command;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeactivateUserCommand {

    @NotBlank(message = "User ID cannot be blank")
    private final String userId;

    private final String reason; // Optional deactivation reason
}
