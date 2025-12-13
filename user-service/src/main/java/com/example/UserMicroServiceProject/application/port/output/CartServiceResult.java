package com.example.UserMicroServiceProject.application.port.output;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartServiceResult {

    private final boolean successful;
    private final String message;

    public static CartServiceResult success() {
        return CartServiceResult.builder()
                .successful(true)
                .message("Cart operation successful")
                .build();
    }

    public static CartServiceResult failure(String message) {
        return CartServiceResult.builder()
                .successful(false)
                .message(message)
                .build();
    }
}
