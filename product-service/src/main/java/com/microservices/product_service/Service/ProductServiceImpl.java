package com.microservices.product_service.Service;

import com.microservices.product_service.DTO.ProductDTO;
import com.microservices.product_service.Entity.Category;
import com.microservices.product_service.Entity.Product;
import com.microservices.product_service.Exception.ProductNotFoundException;
import com.microservices.product_service.Exception.ResourceNotFoundException;
import com.microservices.product_service.Mappers.ProductMapper;
import com.microservices.product_service.Repository.CategoryRepository;
import com.microservices.product_service.Repository.ProductRepository;
import com.microservices.product_service.Request.CreateProductRequest;
import com.microservices.product_service.Response.ProductListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;

    @Override
    public ProductListResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        ProductListResponse response = new ProductListResponse();
        response.setProductDTOS(mapper.toDTOList(products));
        return response;
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        return mapper.toDTO(product);
    }

    @Override
    @Transactional
    public ProductDTO createProduct(CreateProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
        Product product = mapper.toEntity(request);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return mapper.toDTO(savedProduct);
    }

    @Override
    public List<ProductDTO> getProductsByCategoryId(Long categoryId) {
        // Kategori var mı diye kontrol edebiliriz ama Repository boş liste dönerse de sorun olmaz.
        List<Product> products = productRepository.findAllByCategoryId(categoryId);
        return mapper.toDTOList(products);
    }

    @Override
    @Transactional
    public ProductDTO updateStock(Long id, Integer quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        product.setStockQuantity(quantity);

        Product updatedProduct = productRepository.save(product);
        return mapper.toDTO(updatedProduct);
    }

    @Override
    public List<ProductDTO> getProductsByIds(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        return mapper.toDTOList(products);
    }

    @Override
    @Transactional
    public void updatePopularityScore(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        int currentScore = (product.getPopularityScore() == null) ? 0 : product.getPopularityScore();
        product.setPopularityScore(currentScore + quantity);

        productRepository.save(product);
    }
}