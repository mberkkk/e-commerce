package com.microservices.product_service.category.dto.response;

import com.microservices.product_service.category.dto.CategoryDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetCategoriesResponse {
    List<CategoryDTO> categoryDTOS;
}
