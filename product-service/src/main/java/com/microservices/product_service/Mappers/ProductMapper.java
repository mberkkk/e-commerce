package com.microservices.product_service.Mappers;

import com.microservices.product_service.DTO.ProductDTO;
import com.microservices.product_service.Entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductDTO productDTO);

    ProductDTO toDTO(Product product);

    List<Product> toEntityList(List<ProductDTO> productDTOS);

    List<ProductDTO> toDTOList(List<Product> products);
}
