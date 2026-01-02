package com.microservices.product_service.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemAddedToCartEvent {
    private String userId;
    private Long productId;
    private Integer quantity;
    private LocalDateTime timestamp;
}