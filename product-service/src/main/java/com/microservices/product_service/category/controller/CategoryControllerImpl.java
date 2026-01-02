package com.microservices.product_service.category.controller;

import com.microservices.product_service.category.dto.CategoryDTO;
import com.microservices.product_service.category.dto.request.AddCategoryRequest;
import com.microservices.product_service.category.dto.response.GetCategoriesResponse;
import com.microservices.product_service.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/products")
@RestController
@RequiredArgsConstructor
public class CategoryControllerImpl implements CategoryController{
    private final CategoryService categoryService;

    @Override
    @GetMapping("/categories")
    public ResponseEntity<GetCategoriesResponse> listCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Override
    @PostMapping("/categories")
    public ResponseEntity<CategoryDTO> addCategory(@RequestBody AddCategoryRequest request) {
        return ResponseEntity.ok(categoryService.addCategory(request));
    }

    @Override
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
