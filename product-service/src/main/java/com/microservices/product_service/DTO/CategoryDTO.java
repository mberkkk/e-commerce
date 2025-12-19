package com.microservices.product_service.DTO;

import com.microservices.product_service.Entity.CategoryType;
import com.microservices.product_service.Entity.Product;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CategoryDTO extends BaseDTO{
    private Long id;

    private CategoryType type;
}
