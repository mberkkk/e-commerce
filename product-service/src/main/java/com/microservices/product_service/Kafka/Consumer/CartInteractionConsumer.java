package com.microservices.product_service.Kafka.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.product_service.DTO.External.ItemAddedToCartEvent;
import com.microservices.product_service.Service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CartInteractionConsumer {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    // Cart Service'in gönderdiği topic ismi: "item-added-to-cart"
    @KafkaListener(topics = "item-added-to-cart", groupId = "product-service-cart-group")
    public void consumeCartEvent(String message) {
        try {
            log.info("Cart event received: {}", message);

            ItemAddedToCartEvent event = objectMapper.readValue(message, ItemAddedToCartEvent.class);

            if (event.getProductId() != null) {
                // Sepete ekleme olayında popülariteyi 1 artırıyoruz (veya quantity kadar)
                productService.updatePopularityScore(event.getProductId(), 1);
                log.info("Popularity updated for product {}", event.getProductId());
            }

        } catch (Exception e) {
            log.error("Error processing cart event: {}", e.getMessage());
        }
    }
}