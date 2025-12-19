package com.microservices.product_service.Mappers;

import com.microservices.product_service.DTO.CategoryDTO;
import com.microservices.product_service.DTO.ProductDTO;
import com.microservices.product_service.Entity.Category;
import com.microservices.product_service.Entity.Product;
import com.microservices.product_service.Request.AddProductRequest;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Microsoft)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toEntity(ProductDTO productDTO) {
        if ( productDTO == null ) {
            return null;
        }

        Product.ProductBuilder<?, ?> product = Product.builder();

        product.createdAt( productDTO.getCreatedAt() );
        product.updatedAt( productDTO.getUpdatedAt() );
        product.id( productDTO.getId() );
        product.name( productDTO.getName() );
        product.description( productDTO.getDescription() );
        product.price( productDTO.getPrice() );
        product.currency( productDTO.getCurrency() );
        product.stockQuantity( productDTO.getStockQuantity() );
        product.imageUrl( productDTO.getImageUrl() );
        product.popularityScore( productDTO.getPopularityScore() );
        product.category( categoryDTOToCategory( productDTO.getCategory() ) );
        product.isActive( productDTO.getIsActive() );

        return product.build();
    }

    @Override
    public ProductDTO toDTO(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDTO.ProductDTOBuilder<?, ?> productDTO = ProductDTO.builder();

        productDTO.id( product.getId() );
        productDTO.name( product.getName() );
        productDTO.description( product.getDescription() );
        productDTO.price( product.getPrice() );
        productDTO.currency( product.getCurrency() );
        productDTO.stockQuantity( product.getStockQuantity() );
        productDTO.imageUrl( product.getImageUrl() );
        productDTO.popularityScore( product.getPopularityScore() );
        productDTO.category( categoryToCategoryDTO( product.getCategory() ) );
        productDTO.isActive( product.getIsActive() );
        productDTO.createdAt( product.getCreatedAt() );
        productDTO.updatedAt( product.getUpdatedAt() );

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

    @Override
    public Product requestToEntity(AddProductRequest request) {
        if ( request == null ) {
            return null;
        }

        Product.ProductBuilder<?, ?> product = Product.builder();

        if ( request.getCurrency() != null ) {
            product.currency( request.getCurrency() );
        }
        else {
            product.currency( "TL" );
        }
        if ( request.getPopularityScore() != null ) {
            product.popularityScore( request.getPopularityScore() );
        }
        else {
            product.popularityScore( 0 );
        }
        if ( request.getIsActive() != null ) {
            product.isActive( request.getIsActive() );
        }
        else {
            product.isActive( true );
        }
        product.name( request.getName() );
        product.description( request.getDescription() );
        product.price( request.getPrice() );
        product.stockQuantity( request.getStockQuantity() );
        product.imageUrl( request.getImageUrl() );

        return product.build();
    }

    protected Category categoryDTOToCategory(CategoryDTO categoryDTO) {
        if ( categoryDTO == null ) {
            return null;
        }

        Category.CategoryBuilder<?, ?> category = Category.builder();

        category.id( categoryDTO.getId() );
        category.name( categoryDTO.getName() );

        return category.build();
    }

    protected CategoryDTO categoryToCategoryDTO(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryDTO.CategoryDTOBuilder<?, ?> categoryDTO = CategoryDTO.builder();

        categoryDTO.createdAt( category.getCreatedAt() );
        categoryDTO.updatedAt( category.getUpdatedAt() );
        categoryDTO.id( category.getId() );
        categoryDTO.name( category.getName() );

        return categoryDTO.build();
    }
}
