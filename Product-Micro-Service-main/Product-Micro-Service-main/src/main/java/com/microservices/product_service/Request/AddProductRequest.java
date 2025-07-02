package com.microservices.product_service.Request;

import com.microservices.product_service.DTO.ProductDTO;
import com.microservices.product_service.Entity.Category;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
public class AddProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String imageUrl;
    private Integer popularityScore;
    private Boolean isActive;
    private Long categoryCode;
    private String categoryType;
}
