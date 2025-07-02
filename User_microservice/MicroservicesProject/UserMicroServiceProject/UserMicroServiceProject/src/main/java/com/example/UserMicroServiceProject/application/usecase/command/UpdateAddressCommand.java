package com.example.UserMicroServiceProject.application.usecase.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateAddressCommand {
    @NotBlank(message = "User ID cannot be blank")
    private final String userId;

    @NotBlank(message = "Address ID cannot be blank")
    private final String addressId;

    @NotBlank(message = "Address title cannot be blank")
    @Size(max = 50, message = "Address title cannot exceed 50 characters")
    private final String title;

    @NotBlank(message = "Full address cannot be blank")
    @Size(min = 10, max = 500, message = "Full address must be between 10-500 characters")
    private final String fullAddress;

    @NotBlank(message = "City cannot be blank")
    @Size(min = 2, max = 50, message = "City must be between 2-50 characters")
    private final String city;

    @Size(max = 50, message = "District cannot exceed 50 characters")
    private final String district;

    @Size(min = 5, max = 5, message = "Postal code must be 5 digits")
    private final String postalCode;
} 