package com.example.order.order_service.interfaceadapter.controller;

// OrderRequest.java (Place Order için Request Body)

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateOrderRequest {
    @NotBlank(message = "User ID cannot be empty")
    private String userId;

    @NotBlank(message = "Shipping address cannot be empty")
    private String shippingAddress;
}
