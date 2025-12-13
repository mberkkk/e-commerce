package com.example.order.order_service.infrastructure.persistence.client.feign;

import com.example.order.order_service.application.dto.ProductDetailsDto;
import com.example.order.order_service.application.port.out.ProductServiceClientPort;
import com.example.order.order_service.infrastructure.persistence.client.feign.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Profile("!mock") // mock profile değilse bu çalışsın
@RequiredArgsConstructor
@Slf4j
public class ProductServiceFeignAdapter implements ProductServiceClientPort {

    private final ProductServiceFeignClient productServiceFeignClient;

    @Override
    public Map<String, ProductDetailsDto> getProductDetails(List<String> productIds) {
        Map<String, ProductDetailsDto> productDetailsMap = new HashMap<>();

        log.info("🌐 REAL ProductService: Fetching product details for {} products via Feign", productIds.size());

        for (String productId : productIds) {
            try {
                Long productIdLong = Long.parseLong(productId);
                ProductServiceResponse response = productServiceFeignClient.getProduct(productIdLong);

                if (response.getProductDTO() != null) {
                    ProductDetailsDto dto = ProductDetailsDto.builder()
                            .productId(productId)
                            .name(response.getProductDTO().getName())
                            .description(response.getProductDTO().getDescription())
                            .price(response.getProductDTO().getPrice())
                            .stockQuantity(response.getProductDTO().getStockQuantity())
                            .isActive(response.getProductDTO().getIsActive())
                            .build();

                    productDetailsMap.put(productId, dto);
                    log.info("🌐 REAL ProductService: Successfully fetched details for product: {}", productId);
                }
            } catch (Exception e) {
                log.error("🌐 REAL ProductService: Error fetching product details for product: {}", productId, e);
                throw new RuntimeException("Failed to fetch product details for ID: " + productId, e);
            }
        }

        log.info("🌐 REAL ProductService: Successfully fetched details for {} products", productDetailsMap.size());
        return productDetailsMap;
    }

    @Override
    public boolean reserveStock(Map<String, Integer> productQuantities) {
        try {
            String orderReference = java.util.UUID.randomUUID().toString();
            log.info("🌐 REAL ProductService: Reserving stock for order: {} via Feign", orderReference);

            // Product Service'in beklediği formata dönüştür
            List<StockProductInfoServiceDto> stockItems = productQuantities.entrySet().stream()
                    .map(entry -> {
                        StockProductInfoServiceDto item = new StockProductInfoServiceDto();
                        item.setProductId(Long.parseLong(entry.getKey()));
                        item.setWantedQuantity(entry.getValue());
                        return item;
                    })
                    .collect(Collectors.toList());

            ReserveStockServiceRequest request = new ReserveStockServiceRequest();
            request.setOrderReference(orderReference);
            request.setStockProductInfoDTOS(stockItems);

            ReserveStockServiceResponse response = productServiceFeignClient.reserveStock(request);

            // Tüm ürünlerin stoku başarıyla rezerve edildi mi kontrol et
            boolean allReserved = response.getReservationInfoDTOList().stream()
                    .allMatch(info -> info.isStockReservedFlag() && !info.isOutOfStockFlag());

            if (!allReserved) {
                log.warn("🌐 REAL ProductService: Stock reservation failed for order: {}", orderReference);
                response.getReservationInfoDTOList().stream()
                        .filter(info -> !info.isStockReservedFlag())
                        .forEach(info -> log.warn("Failed to reserve stock for product: {} - {}",
                                info.getProductid(), info.getMessage()));
            } else {
                log.info("🌐 REAL ProductService: Stock reservation successful for order: {}", orderReference);
            }

            return allReserved;

        } catch (Exception e) {
            log.error("🌐 REAL ProductService: Error reserving stock via Feign", e);
            return false;
        }
    }

    @Override
    public void releaseStock(Map<String, Integer> productQuantities) {
        try {
            log.info("🌐 REAL ProductService: Stock release requested for {} products via Feign",
                    productQuantities.size());

            // Product Service'te henüz release endpoint'i yok
            // Gelecekte Product Service'e release endpoint'i eklendiğinde burası
            // güncellenecek
            // Şimdilik sadece log
            productQuantities.forEach((productId, quantity) -> log
                    .info("🌐 REAL ProductService: Releasing {} units of product {}", quantity, productId));

        } catch (Exception e) {
            log.error("🌐 REAL ProductService: Error releasing stock via Feign", e);
            throw new RuntimeException("Failed to release stock", e);
        }
    }
}