package com.microservices.product_service.Listener;

import com.microservices.product_service.Entity.Product;
import com.microservices.product_service.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductEventListener {

    private final ProductRepository productRepository;

    @KafkaListener(topics = "item-added-to-cart", groupId = "product-service")
    public void handleItemAddedToCart(String message) {
        try {
            log.info("Received item-added-to-cart event: {}", message);

            // JSON parsing için basit bir yaklaşım (gerçek projede ObjectMapper
            // kullanılmalı)
            // Bu örnekte sadece log yazıyoruz, gerçek implementasyonda:
            // 1. JSON'dan event object'ini parse et
            // 2. Product'ı bul
            // 3. Popularity score'u güncelle

            // Örnek implementasyon:
            // ItemAddedToCartEvent event = objectMapper.readValue(message,
            // ItemAddedToCartEvent.class);
            // Product product =
            // productRepository.findById(event.getProductId()).orElse(null);
            // if (product != null) {
            // product.setPopularityScore(product.getPopularityScore() + 1);
            // productRepository.save(product);
            // log.info("Updated popularity score for product: {}", event.getProductId());
            // }

        } catch (Exception e) {
            log.error("Error processing item-added-to-cart event: {}", e.getMessage(), e);
        }
    }
}