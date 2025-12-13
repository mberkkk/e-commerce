package com.example.order.order_service.application.service;

// CancelOrderService.java (REQ-FUN-005)

import com.example.order.order_service.application.dto.CancelOrderCommand;
import com.example.order.order_service.application.dto.OrderDetailsResponse;
import com.example.order.order_service.application.dto.OrderItemDto;
import com.example.order.order_service.application.port.in.CancelOrderPort;
import com.example.order.order_service.application.port.out.EventPublisherPort;
import com.example.order.order_service.application.port.out.OrderRepositoryPort;
import com.example.order.order_service.application.port.out.ProductServiceClientPort;
import com.example.order.order_service.domain.exception.InvalidOrderStatusTransitionException;
import com.example.order.order_service.domain.exception.OrderNotFoundException;
import com.example.order.order_service.domain.model.Order;
import com.example.order.order_service.domain.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.order.order_service.domain.model.OrderItem;

import java.util.Map;
import java.util.stream.Collectors;

//@Service
@RequiredArgsConstructor
public class CancelOrderApplicationService implements CancelOrderPort {

    private final OrderRepositoryPort orderRepository;
    private final ProductServiceClientPort productService;
    private final EventPublisherPort eventPublisher;

    @Override
    @Transactional // Tüm adımlar atomik olmalı
    public OrderDetailsResponse cancelOrder(String orderId, CancelOrderCommand command) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        // REQ-FUN-005: İptal edilebilir durumu kontrol et
        if (!order.isCancellable()) {
            throw new InvalidOrderStatusTransitionException("Order " + orderId + " is not in a cancellable state.");
        }

        // Stokları serbest bırak (Compensation Action) (REQ-FUN-005)
        Map<String, Integer> productQuantities = order.getItems().stream()
                .collect(Collectors.toMap(OrderItem::getProductId,
                                          OrderItem::getQuantity));
        try {
            productService.releaseStock(productQuantities);
        } catch (Exception e) {
            // REQ-NON-003: Telafi eylemi başarısız olursa (örn: Kafka'ya mesaj gönder, dead-letter queue)
            // Bu basit bir örnek. Gerçekte Saga paternleri veya daha sağlam bir hata yönetimi gerekir.
            System.err.println("Failed to release stock for order " + orderId + ": " + e.getMessage());
            // Sipariş durumunu "Cancellation_Failed" gibi bir duruma çekebiliriz.
            // Şimdilik hata fırlatmaya devam edelim veya başka bir telafi mekanizması devreye sokalım.
            throw new RuntimeException("Failed to cancel order due to stock release failure.", e);
        }

        // Sipariş durumunu güncelle
        order.updateStatus(OrderStatus.CANCELLED);
        Order updatedOrder = orderRepository.save(order);

        // REQ-FUN-005: "OrderCancelled" event'i yayınla
        eventPublisher.publish(new OrderCancelledEvent(
                updatedOrder.getOrderId(), updatedOrder.getCustomerId(), command.getInitiatorId(), command.getReason()));

        return mapToOrderDetailsResponse(updatedOrder);
    }

    private OrderDetailsResponse mapToOrderDetailsResponse(Order order) {
        // ... (PlaceOrderService'deki gibi mapleme)
        return OrderDetailsResponse.builder()
                .orderId(order.getOrderId())
                .customerId(order.getCustomerId())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .lastUpdatedDate(order.getLastUpdatedDate())
                .totalAmount(order.getTotalAmount())
                .currency(order.getCurrency())
                .items(order.getItems().stream()
                        .map(item -> OrderItemDto.builder()
                                .productId(item.getProductId())
                                .productName(item.getCapturedProductName())
                                .unitPrice(item.getCapturedUnitPrice())
                                .quantity(item.getQuantity())
                                .lineItemTotal(item.getLineItemTotal())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public static class OrderCancelledEvent {
        private String orderId;
        private String customerId;
        private String initiatorId;
        private String reason;

        public OrderCancelledEvent(String orderId, String customerId, String initiatorId, String reason) {
            this.orderId = orderId;
            this.customerId = customerId;
            this.initiatorId = initiatorId;
            this.reason = reason;
        }

        // Getters
        public String getOrderId() { return orderId; }
        public String getCustomerId() { return customerId; }
        public String getInitiatorId() { return initiatorId; }
        public String getReason() { return reason; }
    }
}