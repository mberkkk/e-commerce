package com.example.UserMicroServiceProject.application.usecase.response;

import com.example.UserMicroServiceProject.domain.model.user.UserAddress;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class AddressResponse {

    private final String addressId;
    private final String title;
    private final String fullAddress;
    private final String city;
    private final String district;
    private final String postalCode;
    private final boolean isDefault;
    private final boolean isBusinessAddress;
    private final Instant createdAt;

    public static AddressResponse from(UserAddress userAddress) {
        return AddressResponse.builder()
                .addressId(userAddress.getId().getValue())
                .title(userAddress.getAddress().getTitle())
                .fullAddress(userAddress.getAddress().getFullAddress())
                .city(userAddress.getAddress().getCity())
                .district(userAddress.getAddress().getDistrict())
                .postalCode(userAddress.getAddress().getPostalCode())
                .isDefault(userAddress.isDefault())
                .isBusinessAddress(userAddress.getAddress().isBusinessAddress())
                .createdAt(userAddress.getCreatedAt())
                .build();
    }
}
