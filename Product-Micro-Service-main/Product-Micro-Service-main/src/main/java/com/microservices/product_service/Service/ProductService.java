// ProductService.java interface'ine eklenecek metodlar
package com.microservices.product_service.Service;

import com.microservices.product_service.Entity.Product;
import com.microservices.product_service.Request.AddProductRequest;
import com.microservices.product_service.Response.ProductListResponse;
import com.microservices.product_service.Response.ProductResponse;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    // Mevcut metodlar...
    ProductListResponse getProducts();

    ProductResponse getProduct(Long id);

    ProductResponse addProduct(AddProductRequest request);

    ProductResponse updateStock(Long id, AddProductRequest request);

    Optional<Product> findById(Long id);

    ProductListResponse searchProducts(String categoryType, Long categoryCode);

    // ✅ YENİ METODLAR - microservice communication için
    void updatePopularityScore(Long productId, Integer quantity);

    List<Product> getProductsByIds(List<Long> productIds);
}