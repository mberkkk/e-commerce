// application/dto/CartItemDto.java
package com.example.order.order_service.application.dto;

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
