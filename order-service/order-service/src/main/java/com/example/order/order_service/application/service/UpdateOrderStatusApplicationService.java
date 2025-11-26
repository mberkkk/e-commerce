package com.example.order.order_service.application.service;

// UpdateOrderStatusService.java (REQ-FUN-004)

import com.example.order.order_service.application.dto.OrderDetailsResponse;
import com.example.order.order_service.application.dto.OrderItemDto;
import com.example.order.order_service.application.dto.UpdateOrderStatusCommand;
import com.example.order.order_service.application.port.in.UpdateOrderStatusPort;
import com.example.order.order_service.application.port.out.EventPublisherPort;
import com.example.order.order_service.application.port.out.OrderRepositoryPort;
import com.example.order.order_service.domain.exception.OrderNotFoundException;
import com.example.order.order_service.domain.model.Order;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

//@Service
@RequiredArgsConstructor
public class UpdateOrderStatusApplicationService implements UpdateOrderStatusPort {

    //@Qualifier("orderRepositoryPort")
    private final OrderRepositoryPort orderRepository;
    private final EventPublisherPort eventPublisher;

    @Override
    @Transactional
    public OrderDetailsResponse updateOrderStatus(String orderId, UpdateOrderStatusCommand command) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        // REQ-FUN-004: Durum geçiş kuralları Order domain modelinde kontrol edilir
        order.updateStatus(command.getNewStatus());

        Order updatedOrder = orderRepository.save(order);

        // REQ-FUN-004: Event yayınla
        eventPublisher.publish(new OrderStatusUpdatedEvent(
                updatedOrder.getOrderId(), updatedOrder.getCustomerId(),
                updatedOrder.getStatus().name(), command.getNotes(), command.getInitiatedBy()));

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

    public static class OrderStatusUpdatedEvent {
        private String orderId;
        private String customerId;
        private String newStatus;
        private String notes;
        private String initiatedBy;

        public OrderStatusUpdatedEvent(String orderId, String customerId, String newStatus, String notes, String initiatedBy) {
            this.orderId = orderId;
            this.customerId = customerId;
            this.newStatus = newStatus;
            this.notes = notes;
            this.initiatedBy = initiatedBy;
        }

        // Getters
        public String getOrderId() { return orderId; }
        public String getCustomerId() { return customerId; }
        public String getNewStatus() { return newStatus; }
        public String getNotes() { return notes; }
        public String getInitiatedBy() { return initiatedBy; }
    }
}
