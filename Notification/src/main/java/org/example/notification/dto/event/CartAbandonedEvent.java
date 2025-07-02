package org.example.notification.dto.event;

import lombok.Data;
import java.util.List;

@Data
public class CartAbandonedEvent {
    private String userId;
    private String email;
    private List<CartItem> items;
    
    @Data
    public static class CartItem {
        private String productId;
        private String productName;
        private Integer quantity;
        private Double price;
    }
} 