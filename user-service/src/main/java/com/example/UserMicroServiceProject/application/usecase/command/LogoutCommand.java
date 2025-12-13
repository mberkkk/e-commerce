package com.example.UserMicroServiceProject.application.usecase.command;

import lombok.Builder;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
public class LogoutCommand {

    @NotBlank(message = "User ID cannot be blank")
    private String userId;

    @NotBlank(message = "Token cannot be blank")
    private final String token;
}
