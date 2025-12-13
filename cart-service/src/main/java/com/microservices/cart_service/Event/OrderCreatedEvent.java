// OrderCreatedEvent.java (Cart Service içinde)
package com.microservices.cart_service.Event;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class OrderCreatedEvent {
    private String orderId;
    private String userId;
    private List<CartItemDto> items;
    private BigDecimal totalAmount;
    private Instant timestamp;
}