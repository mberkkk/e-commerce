package com.example.order.order_service.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(36)")
    private String orderItemId;
    private String productId;
    private String capturedProductName;
    private BigDecimal capturedUnitPrice;
    private Integer quantity;
    private BigDecimal lineItemTotal;

    @ManyToOne(fetch = FetchType.LAZY) // <-- BURAYI EKLEYİN
    @JoinColumn(name = "order_id", nullable = false) // <-- BURAYI GÜNCELLEYİN
    private OrderEntity order; // <-- BU ALANI DEĞİŞTİRİN (Eskiden 'private String orderId;' idi)

    // ManyToOne ilişkisi OrderEntity'den JoinColumn ile yönetildiği için burada
    // ManyToOne tanımlamıyoruz.
    // Ancak test edip hata alırsanız @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "order_id", insertable = false, updatable = false)
    // ekleyebilirsiniz.
    // Bu senaryoda JoinColumn sahibi OrderEntity olduğu için gerek yok.
}
