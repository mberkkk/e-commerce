package com.example.UserMicroServiceProject.application.usecase.response;

import com.example.UserMicroServiceProject.domain.model.user.User;
import com.example.UserMicroServiceProject.domain.model.valueobject.UserStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder(toBuilder = true) // ✅ toBuilder = true eklendi
public class UserResponse {

    private final String userId;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final UserStatus status;
    private final boolean emailVerified;
    private final boolean phoneVerified;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant lastLoginAt;

    // Optional fields
    private final List<AddressResponse> addresses;

    /**
     * Factory method to create UserResponse from Domain Entity
     */
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .userId(user.getId().getId())
                .email(user.getEmail().getValue())
                .firstName(user.getName().getFirstName())
                .lastName(user.getName().getLastName())
                .phoneNumber(user.getPhoneNumber() != null ? user.getPhoneNumber().getValue() : null)
                .status(user.getStatus())
                .emailVerified(user.getEmailVerified())
                .phoneVerified(user.getPhoneVerified())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }

    /**
     * Factory method with addresses - FIXED
     */
    public static UserResponse fromWithAddresses(User user) {
        List<AddressResponse> addressResponses = user.getAddresses().stream()
                .map(AddressResponse::from)
                .toList();

        // ✅ Düzgün implementation
        UserResponse baseResponse = from(user);
        return baseResponse.toBuilder()
                .addresses(addressResponses)
                .build();
    }
}