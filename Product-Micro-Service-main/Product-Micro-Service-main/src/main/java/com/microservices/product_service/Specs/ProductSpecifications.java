package com.microservices.product_service.Specs;

import com.microservices.product_service.Entity.*;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {
    public static Specification<Product> hasCategoryCode(Long categoryCode) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, Category> categoryJoin = root.join(Product.Fields.category);
            return criteriaBuilder.equal(categoryJoin.get(Category.Fields.categoryCode), categoryCode);
        };
    }

    public static Specification<Product> hasCategoryType(CategoryType categoryType) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, Category> categoryJoin = root.join(Product.Fields.category);
            return criteriaBuilder.equal(categoryJoin.get(Category.Fields.categoryType), categoryType);
        };
    }

}
