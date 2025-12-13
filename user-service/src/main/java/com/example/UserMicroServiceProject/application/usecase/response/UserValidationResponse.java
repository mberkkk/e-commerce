package com.example.UserMicroServiceProject.application.usecase.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserValidationResponse {

    private final boolean valid;
    private final String userId;
    private final String email;
    private final String fullName;
    private final boolean isActive;
    private final boolean canPerformOperations;
    private final String validationMessage;

    public static UserValidationResponse valid(String userId, String email, String fullName) {
        return UserValidationResponse.builder()
                .valid(true)
                .userId(userId)
                .email(email)
                .fullName(fullName)
                .isActive(true)
                .canPerformOperations(true)
                .validationMessage("User is valid")
                .build();
    }

    public static UserValidationResponse invalid(String message) {
        return UserValidationResponse.builder()
                .valid(false)
                .canPerformOperations(false)
                .validationMessage(message)
                .build();
    }
}