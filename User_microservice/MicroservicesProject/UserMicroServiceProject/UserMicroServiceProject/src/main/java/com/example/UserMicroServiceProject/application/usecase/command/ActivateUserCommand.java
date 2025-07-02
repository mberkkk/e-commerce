package com.example.UserMicroServiceProject.application.usecase.command;
import lombok.Builder;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;

@Getter
@Builder
public class ActivateUserCommand {

    @NotBlank(message = "User ID cannot be blank")
    private final String userId;
}