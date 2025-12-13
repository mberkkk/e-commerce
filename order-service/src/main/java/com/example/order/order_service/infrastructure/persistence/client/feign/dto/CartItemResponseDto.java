// CartItemResponseDto.java
package com.example.order.order_service.infrastructure.persistence.client.feign.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemResponseDto {
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}