package com.microservices.product_service.Mappers;

import com.microservices.product_service.DTO.CategoryDTO;
import com.microservices.product_service.Entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toEntity(Category category);

    Category toDTO(CategoryDTO categoryDTO);

    List<CategoryDTO> toDTOList (List<Category> categories);

    List<Category> toEntityList(List<CategoryDTO> categoryDTOS);
}
