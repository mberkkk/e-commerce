package com.microservices.product_service.category.service;

import com.microservices.product_service.category.dto.CategoryDTO;
import com.microservices.product_service.category.dto.request.AddCategoryRequest;
import com.microservices.product_service.category.dto.response.GetCategoriesResponse;
import com.microservices.product_service.category.entity.Category;
import com.microservices.product_service.category.entity.CategoryType;
import com.microservices.product_service.category.mapper.CategoryMapper;
import com.microservices.product_service.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Override
    public GetCategoriesResponse getAllCategories() {
        GetCategoriesResponse response = new GetCategoriesResponse();
        response.setCategoryDTOS(mapper.toDTOList(repository.findAll()));
        return response;
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return repository.getReferenceById(categoryId);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        repository.deleteById(categoryId);
    }

    @Override
    public CategoryDTO addCategory(AddCategoryRequest request) {
        Category category = Category.builder()
                .type(CategoryType.valueOf(request.getType().toUpperCase()))
                .build();

        Category savedCategory = repository.save(category);
        return mapper.toDTO(savedCategory);
    }
}
