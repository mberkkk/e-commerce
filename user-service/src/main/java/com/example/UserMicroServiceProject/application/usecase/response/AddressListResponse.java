package com.example.UserMicroServiceProject.application.usecase.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AddressListResponse {

    private final List<AddressResponse> addresses;
    private final int totalCount;
    private final String defaultAddressId;

    public static AddressListResponse from(List<AddressResponse> addresses) {
        String defaultAddressId = addresses.stream()
                .filter(AddressResponse::isDefault)
                .map(AddressResponse::getAddressId)
                .findFirst()
                .orElse(null);

        return AddressListResponse.builder()
                .addresses(addresses)
                .totalCount(addresses.size())
                .defaultAddressId(defaultAddressId)
                .build();
    }
}
