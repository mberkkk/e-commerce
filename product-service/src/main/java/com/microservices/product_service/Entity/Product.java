package com.microservices.product_service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode; // Eklendi
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Builder
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    @Builder.Default
    private String currency = "TL";
    private Integer stockQuantity;
    private String imageUrl;
    @Builder.Default
    private Integer popularityScore = 0;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @Builder.Default
    private Boolean isActive = true;
}