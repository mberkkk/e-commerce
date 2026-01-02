package com.microservices.product_service.category.service;

import com.microservices.product_service.category.dto.CategoryDTO;
import com.microservices.product_service.category.dto.request.AddCategoryRequest;
import com.microservices.product_service.category.dto.response.GetCategoriesResponse;
import com.microservices.product_service.category.entity.Category;

public interface CategoryService {
    GetCategoriesResponse getAllCategories();

    CategoryDTO addCategory(AddCategoryRequest addCategoryRequest);

    void deleteCategory(Long categoryId);

    Category getCategoryById(Long categoryId);
}
