package com.microservices.product_service.Mappers;

import com.microservices.product_service.DTO.CategoryDTO;
import com.microservices.product_service.DTO.ProductDTO;
import com.microservices.product_service.Entity.Category;
import com.microservices.product_service.Entity.Product;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.50.v20250628-1110, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toEntity(ProductDTO productDTO) {
        if ( productDTO == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.category( categoryDTOToCategory( productDTO.getCategory() ) );
        product.description( productDTO.getDescription() );
        product.id( productDTO.getId() );
        product.imageUrl( productDTO.getImageUrl() );
        product.isActive( productDTO.getIsActive() );
        product.name( productDTO.getName() );
        product.popularityScore( productDTO.getPopularityScore() );
        product.price( productDTO.getPrice() );
        product.stockQuantity( productDTO.getStockQuantity() );

        return product.build();
    }

    @Override
    public ProductDTO toDTO(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDTO.ProductDTOBuilder productDTO = ProductDTO.builder();

        productDTO.category( categoryToCategoryDTO( product.getCategory() ) );
        productDTO.description( product.getDescription() );
        productDTO.id( product.getId() );
        productDTO.imageUrl( product.getImageUrl() );
        productDTO.isActive( product.getIsActive() );
        productDTO.name( product.getName() );
        productDTO.popularityScore( product.getPopularityScore() );
        productDTO.price( product.getPrice() );
        productDTO.stockQuantity( product.getStockQuantity() );

        return productDTO.build();
    }

    @Override
    public List<Product> toEntityList(List<ProductDTO> productDTOS) {
        if ( productDTOS == null ) {
            return null;
        }

        List<Product> list = new ArrayList<Product>( productDTOS.size() );
        for ( ProductDTO productDTO : productDTOS ) {
            list.add( toEntity( productDTO ) );
        }

        return list;
    }

    @Override
    public List<ProductDTO> toDTOList(List<Product> products) {
        if ( products == null ) {
            return null;
        }

        List<ProductDTO> list = new ArrayList<ProductDTO>( products.size() );
        for ( Product product : products ) {
            list.add( toDTO( product ) );
        }

        return list;
    }

    protected Category categoryDTOToCategory(CategoryDTO categoryDTO) {
        if ( categoryDTO == null ) {
            return null;
        }

        Category.CategoryBuilder category = Category.builder();

        category.categoryCode( categoryDTO.getCategoryCode() );
        category.categoryType( categoryDTO.getCategoryType() );
        category.id( categoryDTO.getId() );

        return category.build();
    }

    protected CategoryDTO categoryToCategoryDTO(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryDTO.CategoryDTOBuilder categoryDTO = CategoryDTO.builder();

        categoryDTO.categoryCode( category.getCategoryCode() );
        categoryDTO.categoryType( category.getCategoryType() );
        categoryDTO.id( category.getId() );

        return categoryDTO.build();
    }
}
