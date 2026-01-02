package com.microservices.product_service.product.controller;

import com.microservices.product_service.product.dto.ProductDTO;
import com.microservices.product_service.product.dto.request.CreateProductRequest;
import com.microservices.product_service.product.dto.response.ProductListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/products")
public interface ProductController {

    @GetMapping
    ResponseEntity<ProductListResponse> getAllProducts();

    @GetMapping("/{id}")
    ResponseEntity<ProductDTO> getProductById(@PathVariable Long id);

    @PostMapping
    ResponseEntity<ProductDTO> createProduct(@RequestBody CreateProductRequest request);

    @GetMapping("/category/{categoryId}")
    ResponseEntity<List<ProductDTO>> getProductsByCategoryId(@PathVariable Long categoryId);
}
