package com.example.order.order_service.application.dto;

// PlaceOrderCommand.java

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateOrderCommand {
    @NotBlank
    private String userId;

    @NotBlank
    private String shippingAddress;
}
