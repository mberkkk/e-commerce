package com.microservices.product_service.Mappers;

import com.microservices.product_service.DTO.CategoryDTO;
import com.microservices.product_service.Entity.Category;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.50.v20250628-1110, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryDTO toEntity(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryDTO.CategoryDTOBuilder categoryDTO = CategoryDTO.builder();

        categoryDTO.categoryCode( category.getCategoryCode() );
        categoryDTO.categoryType( category.getCategoryType() );
        categoryDTO.id( category.getId() );

        return categoryDTO.build();
    }

    @Override
    public Category toDTO(CategoryDTO categoryDTO) {
        if ( categoryDTO == null ) {
            return null;
        }

        Category.CategoryBuilder category = Category.builder();

        category.categoryCode( categoryDTO.getCategoryCode() );
        category.categoryType( categoryDTO.getCategoryType() );
        category.id( categoryDTO.getId() );

        return category.build();
    }

    @Override
    public List<CategoryDTO> toDTOList(List<Category> categories) {
        if ( categories == null ) {
            return null;
        }

        List<CategoryDTO> list = new ArrayList<CategoryDTO>( categories.size() );
        for ( Category category : categories ) {
            list.add( toEntity( category ) );
        }

        return list;
    }

    @Override
    public List<Category> toEntityList(List<CategoryDTO> categoryDTOS) {
        if ( categoryDTOS == null ) {
            return null;
        }

        List<Category> list = new ArrayList<Category>( categoryDTOS.size() );
        for ( CategoryDTO categoryDTO : categoryDTOS ) {
            list.add( toDTO( categoryDTO ) );
        }

        return list;
    }
}
