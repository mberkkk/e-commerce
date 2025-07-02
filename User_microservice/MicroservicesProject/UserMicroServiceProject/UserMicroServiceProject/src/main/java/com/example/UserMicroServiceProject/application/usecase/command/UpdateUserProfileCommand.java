package com.example.UserMicroServiceProject.application.usecase.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateUserProfileCommand {

    @NotBlank(message = "User ID cannot be blank")
    private String userId;

    @NotBlank(message = "First name cannot be blank")
    @Size(min = 2, max = 50, message = "First name must be between 2-50 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 2, max = 50, message = "Last name must be between 2-50 characters")
    private String lastName;

    private String phoneNumber; // Optional
}
