package com.microservices.product_service.category.repository;

import com.microservices.product_service.category.entity.Category;
import com.microservices.product_service.category.entity.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

    Optional<Category> findByType(CategoryType type);

}
