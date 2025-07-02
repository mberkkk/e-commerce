package com.microservices.cart_service.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.microservices.cart_service.DTO.ProductDto;
import com.microservices.cart_service.Response.ProductServiceResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalApiService {

    private final ProductServiceClient productServiceClient;

    public boolean checkProductStock(Long productId, Integer quantity) {
        try {
            log.info("Checking stock via Feign: productId={}, quantity={}", productId, quantity);

            ProductServiceResponse response = productServiceClient.getProduct(productId);

            if (response == null || response.getProductDTO() == null) {
                log.warn("Product not found: {}", productId);
                return false;
            }

            ProductDto product = response.getProductDTO();

            boolean hasStock = product.getStockQuantity() != null &&
                    product.getStockQuantity() >= quantity &&
                    Boolean.TRUE.equals(product.getIsActive());

            log.info("Stock check result: productId={}, available={}, requested={}, hasStock={}, isActive={}",
                    productId, product.getStockQuantity(), quantity, hasStock, product.getIsActive());

            return hasStock;

        } catch (Exception e) {
            log.error("Error checking stock via Feign: productId={}, error={}", productId, e.getMessage(), e);
            return false; // Güvenli taraf - hata durumunda false döndür
        }
    }
}