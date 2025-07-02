package com.microservices.cart_service.Entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Embeddable
public class CartItem {
    private Long productId;
    private String productName; // Order Service için gerekli
    private Integer quantity;
    private BigDecimal price; // Order Service için gerekli
    private BigDecimal subtotal; // quantity * price

    // Subtotal'ı otomatik hesaplayalım
    public BigDecimal getSubtotal() {
        if (price != null && quantity != null) {
            return price.multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
    }
}