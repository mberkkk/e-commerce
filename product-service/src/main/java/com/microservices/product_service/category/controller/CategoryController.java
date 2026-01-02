package com.microservices.product_service.category.controller;

import com.microservices.product_service.category.dto.CategoryDTO;
import com.microservices.product_service.category.dto.request.AddCategoryRequest;
import com.microservices.product_service.category.dto.response.GetCategoriesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface CategoryController {


    @GetMapping
    ResponseEntity<GetCategoriesResponse> listCategories();

    @PostMapping
    ResponseEntity<CategoryDTO> addCategory(@RequestBody AddCategoryRequest request);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteCategory(@PathVariable Long id);
}
