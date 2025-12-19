package com.microservices.product_service.Service;

import com.microservices.product_service.DTO.CategoryDTO;
import com.microservices.product_service.Entity.Category;
import com.microservices.product_service.Entity.CategoryType;
import com.microservices.product_service.Request.AddCategoryRequest;
import com.microservices.product_service.Response.GetCategoriesResponse;

import java.util.Optional;

public interface CategoryService {
    GetCategoriesResponse getAllCategories();

    CategoryDTO addCategory(AddCategoryRequest addCategoryRequest);

    void deleteCategory(Long categoryId);

    Category getCategoryByReference(Long categoryId);
}
