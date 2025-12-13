package com.example.order.order_service.application.service;

// PlaceOrderService.java (REQ-FUN-001)

import com.example.order.order_service.application.dto.*;
import com.example.order.order_service.application.port.out.*;
import com.example.order.order_service.domain.exception.InsufficientStockException;
import com.example.order.order_service.domain.model.Order;
import com.example.order.order_service.domain.model.OrderItem;
import com.example.order.order_service.domain.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateOrderApplicationService
        implements com.example.order.order_service.application.port.in.CreateOrderPort {

    // @Qualifier("orderRepositoryPort")
    private final OrderRepositoryPort orderRepositoryPort;
    private final CartServiceClientPort cartServiceClientPort;
    private final ProductServiceClientPort productServiceClientPort;
    private final UserServiceClientPort userServiceClientPort;
    private final EventPublisherPort eventPublisherPort;
    // private final UserServiceClientPort userServiceClientPort; // Add this later

    @Override
    @Transactional // Tüm adımlar atomik olmalı
    public OrderDetailsResponse createOrder(CreateOrderCommand command) {
        log.info("Sipariş oluşturma işlemi başlatıldı. Kullanıcı ID: {}", command.getUserId());


        // Step 3: Cart Service -> Get Cart
        CartDto cart = cartServiceClientPort.getCartByUserId(command.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user " + command.getUserId()));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty for user " + command.getUserId());
        }
        log.info("Kullanıcı sepeti bulundu: {} ürün.", cart.getItems().size());

        // Step 4: Product Service -> Reserve Stock
        Map<String, Integer> productQuantities = cart.getItems().stream()
                .collect(Collectors.toMap(
                        CartItemDto::getProductId,
                        CartItemDto::getQuantity));

        boolean stockReserved = productServiceClientPort.reserveStock(productQuantities);
        if (!stockReserved) {
            log.warn("Stok rezervasyonu başarısız!");
            throw new InsufficientStockException("Stok yetersiz!");
        }
        log.info("Stok rezervasyonu başarılı.");

        // Step 6: Order Service -> Create Order (PENDING)
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setCustomerId(command.getUserId());
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setOrderDate(LocalDateTime.now());
        order.setLastUpdatedDate(LocalDateTime.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setCurrency("TRY");
        order.setItems(cart.getItems().stream().map(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(item.getProductId());
            orderItem.setCapturedProductName(item.getProductName());
            orderItem.setCapturedUnitPrice(item.getUnitPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setLineItemTotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            return orderItem;
        }).collect(Collectors.toList()));

        orderRepositoryPort.save(order);
        log.info("Order kaydı oluşturuldu: {}", order.getOrderId());

        // Step 7: Order Service -> Kafka: "order-created"
        OrderCreatedEvent event = new OrderCreatedEvent(
                order.getOrderId(),
                order.getCustomerId(),
                order.getItems().stream().map(item -> CartItemDto.builder()
                        .productId(item.getProductId())
                        .productName(item.getCapturedProductName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getCapturedUnitPrice())
                        .build()).collect(Collectors.toList()),
                order.getTotalAmount(),
                Instant.now());
        eventPublisherPort.publish(event);
        log.info("Order-created event'i Kafka'ya gönderildi: {}", event);

        return mapToOrderDetailsResponse(order);
    }

    private OrderDetailsResponse mapToOrderDetailsResponse(Order order) {
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
}
