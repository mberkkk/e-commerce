package com.microservices.cart_service.Event;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class CartAbandonedEvent {
    private String userId;
    private LocalDateTime timestamp;
}