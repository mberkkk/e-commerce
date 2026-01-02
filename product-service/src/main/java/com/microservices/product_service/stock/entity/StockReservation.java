package com.microservices.product_service.stock.entity;

import com.microservices.product_service.common.entity.BaseEntity;
import com.microservices.product_service.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "STOCK_RESERVATION")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@EqualsAndHashCode(callSuper = true)
public class StockReservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id") // foreign key sütunu
    private Product product;
    private Integer reservedQuantity;
    private String orderReference; // orderId gibi referans
    private Boolean confirmed;

}
