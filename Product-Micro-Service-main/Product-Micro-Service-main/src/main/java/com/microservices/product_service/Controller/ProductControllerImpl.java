package com.microservices.product_service.Controller;

import com.microservices.product_service.Request.AddProductRequest;
import com.microservices.product_service.Request.CategoryQueryRequest;
import com.microservices.product_service.Response.ProductListResponse;
import com.microservices.product_service.Response.ProductResponse;
import com.microservices.product_service.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/products")
@RestController
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController{
    private final ProductService productService;

    @Override
    @GetMapping
    public ProductListResponse getProducts() {
        return productService.getProducts();
    }

    @Override
    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable("id") Long id) {
        return productService.getProduct(id);
    }

    @Override
    @PostMapping
    public ProductResponse addProduct(@RequestBody AddProductRequest request) {
        return productService.addProduct(request);
    }

    @Override
    @PutMapping("/{id}/stock")
    public ProductResponse updateStock(@PathVariable("id") Long id, @RequestBody AddProductRequest request) {
        return productService.updateStock(id,request);
    }

    @Override
    @PostMapping("/search")
    public ProductListResponse searchProducts(@RequestParam(required = false) String categoryType, @RequestParam(required = false) Long categoryCode) {
        return productService.searchProducts(categoryType,categoryCode);
    }

}
