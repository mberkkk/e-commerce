// ProductService.java interface'ine eklenecek metodlar
package com.microservices.product_service.Service;
import com.microservices.product_service.Request.CreateProductRequest;
import com.microservices.product_service.Response.ProductListResponse;
import java.util.List;
import com.microservices.product_service.DTO.ProductDTO;

public interface ProductService {

    ProductListResponse getAllProducts();

    ProductDTO getProductById(Long id);

    ProductDTO createProduct(CreateProductRequest request);

    List<ProductDTO> getProductsByCategoryId(Long categoryId);

    ProductDTO updateStock(Long id, Integer quantity);

    List<ProductDTO> getProductsByIds(List<Long> productIds);

    void updatePopularityScore(Long productId, Integer quantity);
}