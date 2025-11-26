package com.example.order.order_service.infrastructure.persistence.jpa.adapter;

import com.example.order.order_service.application.port.out.OrderRepositoryPort;
import com.example.order.order_service.domain.model.Order;
import com.example.order.order_service.domain.model.OrderItem;
import com.example.order.order_service.domain.model.OrderStatus; // OrderStatus'u import edin
import com.example.order.order_service.infrastructure.persistence.jpa.entity.OrderEntity;
import com.example.order.order_service.infrastructure.persistence.jpa.entity.OrderItemEntity;
import com.example.order.order_service.infrastructure.persistence.jpa.repository.OrderJpaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component; // Bu kalacak

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component // Bu kalacak
@RequiredArgsConstructor
public class SpringDataOrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order) {
        OrderEntity orderEntity = toEntity(order);
        OrderEntity savedEntity = orderJpaRepository.save(orderEntity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Order> findById(String orderId) {
        return orderJpaRepository.findById(orderId)
                .map(this::toDomain);
    }

    @Override
    public Page<Order> findByCustomerId(String customerId, Pageable pageable) {
        return orderJpaRepository.findByCustomerId(customerId, pageable)
                .map(this::toDomain);
    }

    @Override
    public void delete(Order order) {
        orderJpaRepository.delete(toEntity(order));
    }

    private OrderEntity toEntity(Order domain) {
        OrderEntity orderEntity = OrderEntity.builder()
                .orderId(domain.getOrderId())
                .customerId(domain.getCustomerId())
                .status(domain.getStatus()) // OrderStatus'un import edildiğinden emin olun
                .orderDate(domain.getOrderDate())
                .lastUpdatedDate(domain.getLastUpdatedDate())
                .totalAmount(domain.getTotalAmount())
                .currency(domain.getCurrency())
                .paymentMethodDetails(domain.getPaymentMethodDetails())
                .build();

        // OrderItemEntity'lerin OrderEntity objesini set etme (ÇOK KRİTİK!)
        List<OrderItemEntity> itemEntities = domain.getItems().stream()
                .map(item -> {
                    OrderItemEntity itemEntity = toItemEntity(item);
                    itemEntity.setOrder(orderEntity); // <-- BURAYI GÜNCELLEYİN (order_id yerine OrderEntity objesi)
                    return itemEntity;
                })
                .collect(Collectors.toList());

        orderEntity.setItems(itemEntities); // items listesini OrderEntity'ye set edin

        return orderEntity;
    }

    private OrderItemEntity toItemEntity(OrderItem domain) {
        return OrderItemEntity.builder()
                .orderItemId(domain.getOrderItemId())
                .productId(domain.getProductId())
                .capturedProductName(domain.getCapturedProductName())
                .capturedUnitPrice(domain.getCapturedUnitPrice())
                .quantity(domain.getQuantity())
                .lineItemTotal(domain.getLineItemTotal())
                .build();
    }

    private Order toDomain(OrderEntity entity) {
        List<OrderItem> domainItems = entity.getItems().stream()
                .map(this::toItemDomain)
                .collect(Collectors.toList());

        return Order.builder()
                .orderId(entity.getOrderId())
                .customerId(entity.getCustomerId())
                .status(entity.getStatus()) // OrderStatus'un import edildiğinden emin olun
                .orderDate(entity.getOrderDate())
                .lastUpdatedDate(entity.getLastUpdatedDate())
                .totalAmount(entity.getTotalAmount())
                .currency(entity.getCurrency())
                .items(domainItems)
                .paymentMethodDetails(entity.getPaymentMethodDetails())
                .build();
    }

    private OrderItem toItemDomain(OrderItemEntity entity) {
        return OrderItem.builder()
                .orderItemId(entity.getOrderItemId())
                // order_id'ye OrderEntity üzerinden erişim
                // .orderId(entity.getOrder().getOrderId()) // Eğer OrderItem'ın orderId alanı varsa (şimdilik yok, DTO'da var)
                .productId(entity.getProductId())
                .capturedProductName(entity.getCapturedProductName())
                .capturedUnitPrice(entity.getCapturedUnitPrice())
                .quantity(entity.getQuantity())
                .lineItemTotal(entity.getLineItemTotal())
                .build();
    }
}