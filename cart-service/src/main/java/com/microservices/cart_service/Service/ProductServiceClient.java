package com.microservices.cart_service.Service;

import com.microservices.cart_service.Response.ProductServiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${services.product.base-url:http://product-service-app:8080}", path = "/api/products")
public interface ProductServiceClient {

    @GetMapping("/{id}")
    ProductServiceResponse getProduct(@PathVariable("id") Long id);
}