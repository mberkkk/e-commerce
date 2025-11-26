// CartDto.java
package com.example.order.order_service.infrastructure.persistence.client.feign.dto;

import lombok.Data;
import java.util.List;

@Data
public class FeignCartDto {
    private Long id;
    private String userId;
    private List<CartItemResponseDto> cartItems;
}