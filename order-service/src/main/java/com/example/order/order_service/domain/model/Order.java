package com.example.order.order_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    private String orderId;
    private String customerId;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private LocalDateTime lastUpdatedDate;
    private BigDecimal totalAmount;
    private String currency;
    private List<OrderItem> items;
    private String paymentMethodDetails; // Ödeme referansı vb.

    public Order(String customerId, String currency, String paymentMethodDetails) {
        this.orderId = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.status = OrderStatus.PENDING_PAYMENT; // Başlangıç durumu
        this.orderDate = LocalDateTime.now();
        this.lastUpdatedDate = LocalDateTime.now();
        this.totalAmount = BigDecimal.ZERO;
        this.currency = currency;
        this.items = new ArrayList<>();
        this.paymentMethodDetails = paymentMethodDetails;
    }

    public void addItem(OrderItem item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);
        item.calculateLineItemTotal(); // Kalem toplamını hesapla
        calculateTotalAmount(); // Sipariş toplamını güncelle
    }

    public void calculateTotalAmount() {
        this.totalAmount = items.stream()
                .map(OrderItem::getLineItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void updateStatus(OrderStatus newStatus) {
        // REQ-FUN-004: Durum geçiş kurallarını burada tanımlayın
        if (!isValidStatusTransition(this.status, newStatus)) {
            throw new com.example.order.order_service.domain.exception.InvalidOrderStatusTransitionException(
                    "Cannot transition order from " + this.status + " to " + newStatus);
        }
        this.status = newStatus;
        this.lastUpdatedDate = LocalDateTime.now();
    }

    private boolean isValidStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        // Örnek geçiş kuralları
        return switch (currentStatus) {
            case PENDING_PAYMENT -> newStatus == OrderStatus.PROCESSING || newStatus == OrderStatus.FAILED;
            case PROCESSING -> newStatus == OrderStatus.CONFIRMED || newStatus == OrderStatus.CANCELLED || newStatus == OrderStatus.FAILED;
            case CONFIRMED -> newStatus == OrderStatus.SHIPPED || newStatus == OrderStatus.CANCELLED;
            case SHIPPED -> newStatus == OrderStatus.DELIVERED;
            case DELIVERED -> false; // Genellikle teslim sonrası başka bir duruma geçmez
            case CANCELLED, FAILED, REFUNDED -> false; // Bu durumlar terminal durumlar
        };
    }

    public boolean isCancellable() {
        // REQ-FUN-005: İptal edilebilir durumlar
        return this.status == OrderStatus.PENDING_PAYMENT ||
               this.status == OrderStatus.PROCESSING ||
               this.status == OrderStatus.CONFIRMED;
    }
}
