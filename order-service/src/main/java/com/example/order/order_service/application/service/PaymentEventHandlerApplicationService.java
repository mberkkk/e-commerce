package com.example.order.order_service.application.service;

// PaymentEventHandlerService.java (REQ-FUN-006)

import com.example.order.order_service.application.dto.PaymentEvent;
import com.example.order.order_service.application.port.in.HandlePaymentEventPort;
import com.example.order.order_service.application.port.out.EventPublisherPort;
import com.example.order.order_service.application.port.out.OrderRepositoryPort;
import com.example.order.order_service.domain.exception.OrderNotFoundException;
import com.example.order.order_service.domain.model.Order;
import com.example.order.order_service.domain.model.OrderStatus;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//@Service
@RequiredArgsConstructor
public class PaymentEventHandlerApplicationService implements HandlePaymentEventPort {

    //@Qualifier("orderRepositoryPort")
    private final OrderRepositoryPort orderRepository;
    private final EventPublisherPort eventPublisher; // Durum değişimini de yayınlamak için

    @Override
    @Transactional
    public void handlePaymentEvent(PaymentEvent event) {
        // REQ-NON-010: Idempotency: Duplicate mesajları işlemek için bir mekanizma ekleyin
        // Örn: Event ID'sini kontrol etmek ve daha önce işlenip işlenmediğini kaydetmek.
        // Bu örnekte basitleştirilmiştir.

        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(event.getOrderId()));

        OrderStatus newStatus;
        switch (event.getType()) {
            case PAYMENT_CAPTURED:
                newStatus = OrderStatus.CONFIRMED; // Ödeme alındı, sipariş onaylandı
                break;
            case PAYMENT_AUTHORIZATION_FAILED:
            case PAYMENT_FAILED_GENERIC:
                newStatus = OrderStatus.FAILED; // Ödeme başarısız
                break;
            case PAYMENT_REFUNDED:
                newStatus = OrderStatus.REFUNDED; // Para iadesi yapıldı
                break;
            default:
                // Bilinmeyen event tipi, logla ve ignore et
                System.err.println("Unknown payment event type: " + event.getType());
                return;
        }

        // Sadece ilgili duruma geçiş yapılıyorsa güncelle
        if (order.getStatus() != newStatus) {
            try {
                order.updateStatus(newStatus);
                orderRepository.save(order);

                // Durum değişimini de event olarak yayınla
                eventPublisher.publish(new UpdateOrderStatusApplicationService.OrderStatusUpdatedEvent(
                        order.getOrderId(), order.getCustomerId(), newStatus.name(),
                        "Payment Event: " + event.getType().name(), "PaymentService"));

            } catch (com.example.order.order_service.domain.exception.InvalidOrderStatusTransitionException e) {
                System.err.println("Invalid status transition for order " + order.getOrderId() + ": " + e.getMessage());
                // Hatalı durum geçişleri için loglama veya uyarı
            }
        }
    }
}
