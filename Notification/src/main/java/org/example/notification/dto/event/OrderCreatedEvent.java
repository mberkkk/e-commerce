package org.example.notification.dto.event;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderCreatedEvent {
    private String orderId;
    private String userId;
    private String email;
    private BigDecimal totalAmount;
} 