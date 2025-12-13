package com.example.UserMicroServiceProject.application.usecase.command;

import lombok.Builder;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ValidateUserCommand {

    @NotBlank(message = "User ID cannot be blank")
    private final String userId;

    private final boolean checkActiveStatus; // Check if user is active
}