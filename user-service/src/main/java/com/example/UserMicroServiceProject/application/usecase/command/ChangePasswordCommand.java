package com.example.UserMicroServiceProject.application.usecase.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class ChangePasswordCommand {

    @NotBlank(message = "User ID cannot be blank")
    private final String userId;

    @NotBlank(message = "Current password cannot be blank")
    private final String currentPassword;

    @NotBlank(message = "New password cannot be blank")
    @Size(min = 8, max = 128, message = "New password must be between 8-128 characters")
    private final String newPassword;

    @Override
    public String toString() {
        return "ChangePasswordCommand{" +
                "userId='" + userId + '\'' +
                ", currentPassword='[PROTECTED]'" +
                ", newPassword='[PROTECTED]'" +
                '}';
    }
}