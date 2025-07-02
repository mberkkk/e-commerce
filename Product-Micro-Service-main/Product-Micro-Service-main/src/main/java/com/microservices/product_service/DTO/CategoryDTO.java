package com.microservices.product_service.DTO;

import com.microservices.product_service.Entity.CategoryType;
import com.microservices.product_service.Entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO extends BaseDTO{
    private Long id;
    private Long categoryCode;
    private CategoryType categoryType;
//    private List<ProductDTO> products = new ArrayList<>();
}
