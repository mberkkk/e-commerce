// OrderCreatedEvent.java (Cart Service içinde)
package com.microservices.cart_service.Event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrderCreatedEvent {
    private String orderId;
    private String userId;
    private List<CartItemDto> items;
    private BigDecimal totalAmount;
    private Instant timestamp;
}