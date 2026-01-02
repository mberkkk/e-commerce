// ProductService.java interface'ine eklenecek metodlar
package com.microservices.product_service.product.service;

import com.microservices.product_service.product.dto.ProductDTO;
import com.microservices.product_service.product.dto.request.CreateProductRequest;
import com.microservices.product_service.product.dto.response.ProductListResponse;

import java.util.List;

public interface ProductService {

    ProductListResponse getAllProducts();

    ProductDTO getProductById(Long id);

    ProductDTO createProduct(CreateProductRequest request);

    List<ProductDTO> getProductsByCategoryId(Long categoryId);

    ProductDTO updateStock(Long id, Integer quantity);

    List<ProductDTO> getProductsByIds(List<Long> productIds);

    void updatePopularityScore(Long productId, Integer quantity);
}