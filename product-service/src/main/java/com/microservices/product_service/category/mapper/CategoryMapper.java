package com.microservices.product_service.category.mapper;

import com.microservices.product_service.category.dto.CategoryDTO;
import com.microservices.product_service.category.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toDTO(Category category);

    Category toEntity(CategoryDTO categoryDTO);

    List<CategoryDTO> toDTOList (List<Category> categories);

    List<Category> toEntityList(List<CategoryDTO> categoryDTOS);
}
