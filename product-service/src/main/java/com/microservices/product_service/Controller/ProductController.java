package com.microservices.product_service.Controller;

import com.microservices.product_service.DTO.ProductDTO;
import com.microservices.product_service.Request.CreateProductRequest;
import com.microservices.product_service.Response.ProductListResponse;
import com.microservices.product_service.Response.ProductResponse;
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
