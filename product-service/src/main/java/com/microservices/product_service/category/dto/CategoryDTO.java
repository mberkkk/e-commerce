package com.microservices.product_service.category.dto;

import com.microservices.product_service.category.entity.CategoryType;
import com.microservices.product_service.common.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder // <--- Use SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO extends BaseDTO {
    private Long id;

    private CategoryType type;
}
