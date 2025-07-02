package org.example.notification.dto.event;

import lombok.Data;

@Data
public class ProductStockUpdatedEvent {
    private String userId;
    private String email;
    private String productId;
    private String productName;
    private Integer newStock;
} 