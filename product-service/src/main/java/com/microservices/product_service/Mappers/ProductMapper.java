package com.microservices.product_service.Mappers;

import com.microservices.product_service.DTO.ProductDTO;
import com.microservices.product_service.Entity.Product;
import com.microservices.product_service.Request.AddProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductDTO productDTO);

    ProductDTO toDTO(Product product);

    List<Product> toEntityList(List<ProductDTO> productDTOS);

    List<ProductDTO> toDTOList(List<Product> products);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "currency", defaultValue = "TL")
    @Mapping(target = "popularityScore", defaultValue = "0")
    @Mapping(target = "isActive", defaultValue = "true")
    Product requestToEntity(AddProductRequest request);
}
