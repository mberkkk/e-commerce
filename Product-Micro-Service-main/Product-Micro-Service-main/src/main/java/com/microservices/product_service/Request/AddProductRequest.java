package com.microservices.product_service.Request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private String currency;
    private Integer stockQuantity;
    private String imageUrl;
    private Integer popularityScore;
    private Boolean isActive;
    private Long categoryCode;
    private String categoryType;
}
