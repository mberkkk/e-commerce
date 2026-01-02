package com.microservices.product_service.stock.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.product_service.common.event.OrderCreatedEvent;
import com.microservices.product_service.common.event.OrderEventItemDTO;
import com.microservices.product_service.product.dto.ProductDTO;
import com.microservices.product_service.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedConsumer {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    // Order Service'in topic ismini buraya yazmalısın (Örn: "order-created")
    @KafkaListener(topics = "order-created", groupId = "product-service-order-group")
    public void consumeOrderEvent(String message) {
        try {
            log.info("Order created event received: {}", message);

            OrderCreatedEvent event = objectMapper.readValue(message, OrderCreatedEvent.class);

            for (OrderEventItemDTO item : event.getItems()) {
                try {
                    // String ID'yi Long'a çeviriyoruz (Product Service Long kullanıyor)
                    Long productId = Long.parseLong(item.getProductId());
                    Integer quantitySold = item.getQuantity();

                    // 1. Mevcut ürünü çek
                    ProductDTO product = productService.getProductById(productId);

                    // 2. Yeni stoğu hesapla
                    int newStock = product.getStockQuantity() - quantitySold;
                    if (newStock < 0) newStock = 0; // Negatif stok koruması

                    // 3. Güncellemeleri Yap (Stok Düş + Popülarite Artır)
                    productService.updateStock(productId, newStock);
                    productService.updatePopularityScore(productId, quantitySold); // Satış kadar puan artır

                    log.info("Processed product {} for order {}: Stock -> {}, Popularity +{}",
                            productId, event.getOrderId(), newStock, quantitySold);

                } catch (NumberFormatException e) {
                    log.error("Invalid Product ID format in order: {}", item.getProductId());
                } catch (Exception e) {
                    log.error("Error updating product {} from order: {}", item.getProductId(), e.getMessage());
                }
            }

        } catch (Exception e) {
            log.error("Error processing order event: {}", e.getMessage());
        }
    }
}