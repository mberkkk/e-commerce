package com.example.UserMicroServiceProject.application.port.output;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderServiceResult {

    private final boolean successful;
    private final Integer orderCount;
    private final String errorMessage;

    public static OrderServiceResult success(Integer orderCount) {
        return OrderServiceResult.builder()
                .successful(true)
                .orderCount(orderCount)
                .build();
    }

    public static OrderServiceResult failure(String errorMessage) {
        return OrderServiceResult.builder()
                .successful(false)
                .errorMessage(errorMessage)
                .build();
    }
}
