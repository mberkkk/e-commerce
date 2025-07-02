package com.microservices.cart_service.Event;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ItemAddedToCartEvent {
    private String userId;
    private Long productId;
    private Integer quantity;
    private LocalDateTime timestamp;
}