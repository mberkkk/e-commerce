package com.example.order.order_service.application.port.out;

// ProductServicePort.java (Inventory/Stock)
import java.util.List;
import com.example.order.order_service.application.dto.ProductDetailsDto;

import java.util.Map;

public interface ProductServiceClientPort {
    boolean reserveStock(Map<String, Integer> productQuantities); // Map<ProductId, Quantity>
    void releaseStock(Map<String, Integer> productQuantities);
    // Ürün detaylarını almak için
    Map<String, ProductDetailsDto> getProductDetails(List<String> productIds); // REQ-FUN-001
}

