package com.microservices.product_service.Repository;

import com.microservices.product_service.Entity.Category;
import com.microservices.product_service.Entity.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
    Optional<Category> findByCategoryCode(Long categoryCode);

    Optional<Category> findByCategoryType(CategoryType categoryType);

}
