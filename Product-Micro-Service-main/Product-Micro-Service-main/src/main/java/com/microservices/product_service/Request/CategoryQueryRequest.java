package com.microservices.product_service.Request;

import com.microservices.product_service.Entity.CategoryType;
import lombok.Data;

@Data
public class CategoryQueryRequest {
    private String categoryType;
    private Long categoryCode;
}
