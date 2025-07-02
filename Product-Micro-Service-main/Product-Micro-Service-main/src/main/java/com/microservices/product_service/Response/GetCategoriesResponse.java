package com.microservices.product_service.Response;

import com.microservices.product_service.DTO.CategoryDTO;
import com.microservices.product_service.Entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetCategoriesResponse {
    List<CategoryDTO> categoryDTOS;
}
