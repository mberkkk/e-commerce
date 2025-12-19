package com.microservices.product_service.Controller;

import com.microservices.product_service.DTO.CategoryDTO;
import com.microservices.product_service.Request.AddCategoryRequest;
import com.microservices.product_service.Response.GetCategoriesResponse;
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
