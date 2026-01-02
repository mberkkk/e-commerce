package com.microservices.product_service.product.service;

import com.microservices.product_service.category.entity.Category;
import com.microservices.product_service.category.service.CategoryService;
import com.microservices.product_service.product.dto.ProductDTO;
import com.microservices.product_service.product.dto.request.CreateProductRequest;
import com.microservices.product_service.product.dto.response.ProductListResponse;
import com.microservices.product_service.product.entity.Product;
import com.microservices.product_service.product.exception.ProductNotFoundException;
import com.microservices.product_service.product.mapper.ProductMapper;
import com.microservices.product_service.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductMapper mapper;

    @Override
    public ProductListResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        // Tip: Consider pagination here for production! (.findAll(Pageable))
        ProductListResponse response = new ProductListResponse();
        response.setProductDTOS(mapper.toDTOList(products));
        return response;
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = findProductOrThrow(id);
        return mapper.toDTO(product);
    }

    @Override
    @Transactional
    public ProductDTO createProduct(CreateProductRequest request) {
        // Delegate validation to the expert
        Category category = categoryService.getCategoryById(request.getCategoryId());

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity()) // Or default to 0
                .category(category) // Association set immediately
                .imageUrl(request.getImageUrl())
                .isActive(true)
                .popularityScore(0)
                .build();

        Product savedProduct = productRepository.save(product);
        return mapper.toDTO(savedProduct);
    }
    @Override
    public List<ProductDTO> getProductsByCategoryId(Long categoryId) {
        List<Product> products = productRepository.findAllByCategoryId(categoryId);
        return mapper.toDTOList(products);
    }

    @Override
    @Transactional
    public ProductDTO updateStock(Long id, Integer quantity) {
        Product product = findProductOrThrow(id);

        // RICH DOMAIN: The Service doesn't know "how" to update stock safely. The Entity does.
        // This method inside Product should check if (quantity < 0).
        product.updateInventory(quantity);

        // No need to call save() explicitly if inside @Transactional (Dirty Checking),
        // but keeping it is fine for clarity.
        return mapper.toDTO(productRepository.save(product));
    }

    @Override
    public List<ProductDTO> getProductsByIds(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        return mapper.toDTOList(products);
    }

    @Override
    @Transactional
    public void updatePopularityScore(Long productId, Integer scoreDelta) {
        Product product = findProductOrThrow(productId);

        // RICH DOMAIN: The math happens inside the entity.
        // Prevents Service from accidentally setting score to a negative value or null.
        product.increasePopularity(scoreDelta);

        productRepository.save(product);
    }

    // Helper method to remove code duplication (DRY Principle)
    private Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }
}


