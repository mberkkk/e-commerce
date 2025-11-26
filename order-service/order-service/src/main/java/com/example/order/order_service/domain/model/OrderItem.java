package com.example.order.order_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    private String orderItemId;
    private String productId;
    private String capturedProductName; // Ürün detayları sipariş anında kaydedilir
    private BigDecimal capturedUnitPrice; // Fiyat sipariş anında kaydedilir
    private Integer quantity;
    private BigDecimal lineItemTotal;

    // İş mantığına özgü metodlar eklenebilir (örn: KDV hesaplama, indirim uygulama)
    public void calculateLineItemTotal() {
        if (capturedUnitPrice != null && quantity != null) {
            this.lineItemTotal = capturedUnitPrice.multiply(BigDecimal.valueOf(quantity));
        } else {
            this.lineItemTotal = BigDecimal.ZERO;
        }
    }
}