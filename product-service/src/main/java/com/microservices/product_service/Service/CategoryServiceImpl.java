package com.microservices.product_service.Service;

import com.microservices.product_service.DTO.CategoryDTO;
import com.microservices.product_service.Entity.Category;
import com.microservices.product_service.Entity.CategoryType;
import com.microservices.product_service.Mappers.CategoryMapper;
import com.microservices.product_service.Repository.CategoryRepository;
import com.microservices.product_service.Request.AddCategoryRequest;
import com.microservices.product_service.Response.GetCategoriesResponse;
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
    public Category getCategoryByReference(Long categoryId) {
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
