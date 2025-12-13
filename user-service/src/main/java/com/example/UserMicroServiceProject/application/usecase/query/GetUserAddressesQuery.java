package com.example.UserMicroServiceProject.application.usecase.query;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetUserAddressesQuery {

    @NotBlank(message = "User ID cannot be blank")
    private final String userId;

    private final boolean includeInactive; // Include non-default addresses
}
