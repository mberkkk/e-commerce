package com.example.UserMicroServiceProject.application.usecase.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginCommand {

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    private final String email;

    @NotBlank(message = "Password cannot be blank")
    private final String password;

    private final String userAgent; // Optional - for security tracking
    private final String ipAddress; // Optional - for security tracking

    @Override
    public String toString() {
        return "LoginCommand{" +
                "email='" + email + '\'' +
                ", password='[PROTECTED]'" +
                ", userAgent='" + userAgent + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                '}';
    }
}
