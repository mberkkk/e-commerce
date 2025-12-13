package com.example.order.order_service.infrastructure.persistence.client.feign;

import com.example.order.order_service.infrastructure.persistence.client.feign.dto.ProductServiceResponse;
import com.example.order.order_service.infrastructure.persistence.client.feign.dto.ReserveStockServiceRequest;
import com.example.order.order_service.infrastructure.persistence.client.feign.dto.ReserveStockServiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service", url = "${services.product-service.url:http://localhost:8080}")
public interface ProductServiceFeignClient {

    @GetMapping("/api/products/{productId}")
    ProductServiceResponse getProduct(@PathVariable Long productId);

    @PostMapping("/api/products/reserve-stock")
    ReserveStockServiceResponse reserveStock(@RequestBody ReserveStockServiceRequest request);
}