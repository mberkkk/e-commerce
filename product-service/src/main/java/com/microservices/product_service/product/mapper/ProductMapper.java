package com.microservices.product_service.product.mapper;

import com.microservices.product_service.product.dto.ProductDTO;
import com.microservices.product_service.product.dto.request.CreateProductRequest;
import com.microservices.product_service.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toDTO(Product product);

    List<ProductDTO> toDTOList(List<Product> products);

    // Request -> Entity Dönüşümü
    // MapStruct'a diyoruz ki: "Bu alanları request'te arama, null geç."
    // Entity sınıfındaki @Builder.Default devreye girip bunları dolduracak.

    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "popularityScore", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(CreateProductRequest request);
}