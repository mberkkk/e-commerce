package com.example.UserMicroServiceProject.application.usecase.command;
import lombok.Builder;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class RefreshTokenCommand {

    @NotBlank(message = "Refresh token cannot be blank")
    private final String refreshToken;
}
