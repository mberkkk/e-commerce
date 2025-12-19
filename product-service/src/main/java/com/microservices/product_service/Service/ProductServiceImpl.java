package com.microservices.product_service.Service;

import com.microservices.product_service.Entity.Category;
import com.microservices.product_service.Entity.Product;
import com.microservices.product_service.Mappers.ProductMapper;
import com.microservices.product_service.Repository.ProductRepository;
import com.microservices.product_service.Request.AddProductRequest;
import com.microservices.product_service.Response.ProductListResponse;
import com.microservices.product_service.Response.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final CategoryService categoryService;

    @Override
    public ProductListResponse getProducts() {
        ProductListResponse response = new ProductListResponse();
        response.setProductDTOS(mapper.toDTOList(repository.findAll()));
        return response;
    }

    @Override
    public ProductResponse getProduct(Long id) {
        ProductResponse response = new ProductResponse();
        repository.findById(id)
                .map(mapper::toDTO)
                .ifPresent(response::setProductDTO);

        return response;
    }

    @Override
    @Transactional
    public ProductResponse addProduct(AddProductRequest request) {
        Product product = mapper.requestToEntity(request);

        if (request.getCategoryId() != null) {
            Category category = categoryService.getCategoryByReference(request.getCategoryId());
            product.setCategory(category);
        }
        Product updatedProduct = repository.save(product);

        ProductResponse response = new ProductResponse();
        response.setProductDTO(mapper.toDTO(updatedProduct));
        return response;
    }


    @Override
    @Transactional
    public ProductResponse updateStock(Long id, AddProductRequest request) {
        ProductResponse response = new ProductResponse();
        Optional<Product> optProduct = repository.findById(id);
        if (optProduct.isPresent()) {
            Product product = optProduct.get();
            product.setStockQuantity(request.getStockQuantity());
            Product updatedProduct = repository.save(product);
            response.setProductDTO(mapper.toDTO(updatedProduct));
        }
        return response;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ProductListResponse searchProducts(Long categoryId) {
        ProductListResponse response = new ProductListResponse();

        // EĞER ID VARSA DİREKT ID İLE ÇEK (En hızlısı)
        if (categoryId != null) {
            List<Product> products = repository.findAllByCategoryId(categoryId);
            response.setProductDTOS(mapper.toDTOList(products));
        }

        return response;
    }

    // ✅ YENİ METOD: Popularity score güncelleme
    @Override
    @Transactional
    public void updatePopularityScore(Long productId, Integer quantity) {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        // Basit popülarite skorlama
        Integer currentScore = product.getPopularityScore() != null ? product.getPopularityScore() : 0;
        product.setPopularityScore(currentScore + quantity);
        repository.save(product);

        log.info("Updated popularity score for product {}: {} -> {}",
                productId, currentScore, product.getPopularityScore());
    }

    // ✅ YENİ METOD: Birden fazla ürünün detaylarını getir (Order Service için)
    @Override
    public List<Product> getProductsByIds(List<Long> productIds) {
        return repository.findAllById(productIds);
    }

    // ✅ ReservationConsumer için popularity update
    @Transactional
    public void updatePopularityOnPurchase(Long productId, Integer quantity) {
        updatePopularityScore(productId, quantity);
    }
}