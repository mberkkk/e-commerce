package com.microservices.cart_service.Event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private String productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
}