package com.example.UserMicroServiceProject.application.usecase.command;

import lombok.Builder;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Builder
public class UpdatePersonalInfoCommand {

    @NotBlank(message = "User ID cannot be blank")
    private final String userId;

    @NotBlank(message = "First name cannot be blank")
    @Size(min = 2, max = 50, message = "First name must be between 2-50 characters")
    private final String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 2, max = 50, message = "Last name must be between 2-50 characters")
    private final String lastName;

    private final String phoneNumber; // Optional
}
