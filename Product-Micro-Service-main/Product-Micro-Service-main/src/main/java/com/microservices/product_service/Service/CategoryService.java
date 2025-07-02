package com.microservices.product_service.Service;

import com.microservices.product_service.Entity.Category;
import com.microservices.product_service.Entity.CategoryType;
import com.microservices.product_service.Request.CategoryQueryRequest;
import com.microservices.product_service.Response.GetCategoriesResponse;
import com.microservices.product_service.Response.ProductListResponse;

import java.util.Optional;

public interface CategoryService {
    GetCategoriesResponse getCategories();

    Optional<Category> getCategoryByCode(Long categoryCode);

    Optional<Category> getCategoryByType(CategoryType categoryType);
}
