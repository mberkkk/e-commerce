package com.microservices.product_service.Controller;

import com.microservices.product_service.DTO.ProductDTO;
import com.microservices.product_service.Request.CreateProductRequest;
import com.microservices.product_service.Response.ProductListResponse;
import com.microservices.product_service.Response.ProductResponse;
import com.microservices.product_service.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/products")
@RestController
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController{
    private final ProductService productService;

    @GetMapping
    @Override
    public ResponseEntity<ProductListResponse> getAllProducts() {
        ProductListResponse response = productService.getAllProducts();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO productDTO = productService.getProductById(id);
        return ResponseEntity.ok(productDTO);
    }
    @PostMapping
    @Override
    public ResponseEntity<ProductDTO> createProduct(@RequestBody CreateProductRequest request) {
        ProductDTO createdProduct = productService.createProduct(request);
        return ResponseEntity.ok(createdProduct);
    }

    @GetMapping("/category/{categoryId}")
    @Override
    public ResponseEntity<java.util.List<ProductDTO>> getProductsByCategoryId(@PathVariable Long
    categoryId) {
            java.util.List<ProductDTO> products = productService.getProductsByCategoryId(categoryId);
            return ResponseEntity.ok(products);
        }

}
