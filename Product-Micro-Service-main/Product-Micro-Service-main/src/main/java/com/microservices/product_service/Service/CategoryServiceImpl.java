package com.microservices.product_service.Service;

import com.microservices.product_service.Entity.Category;
import com.microservices.product_service.Entity.CategoryType;
import com.microservices.product_service.Mappers.CategoryMapper;
import com.microservices.product_service.Repository.CategoryRepository;
import com.microservices.product_service.Request.CategoryQueryRequest;
import com.microservices.product_service.Response.GetCategoriesResponse;
import com.microservices.product_service.Response.ProductListResponse;
import com.microservices.product_service.Specs.ProductSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Override
    public GetCategoriesResponse getCategories() {
        GetCategoriesResponse response = new GetCategoriesResponse();
        response.setCategoryDTOS(mapper.toDTOList(repository.findAll()));
        return response;
    }

    @Override
    public Optional<Category> getCategoryByCode(Long categoryCode) {
        return repository.findByCategoryCode(categoryCode);
    }

    @Override
    public Optional<Category> getCategoryByType(CategoryType categoryType) {
        return repository.findByCategoryType(categoryType);
    }


}
