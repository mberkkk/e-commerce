package com.example.order.order_service.infrastructure.persistence.client.feign.dto;

import lombok.Data;

@Data
public class CartServiceResponse {
    private FeignCartDto cartDto;
    private boolean success;
    private String message;
}