package com.example.order.order_service.infrastructure.persistence.event.consumer;

// PaymentEventKafkaConsumer.java (REQ-FUN-006)

import com.example.order.order_service.application.dto.PaymentEvent;
import com.example.order.order_service.application.port.in.HandlePaymentEventPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventKafkaConsumer {

    private final HandlePaymentEventPort handlePaymentEventUseCase;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    // REQ-NON-010: Idempotency için Kafka Consumer'ın idempotent olması sağlanmalı
    // KafkaListener'ın id'sini ve transaction manager'ı kullanarak.
    // Gerçek Kafka için @KafkaListener(topics = "payment.PaymentEvents", groupId = "order-service-group", containerFactory = "kafkaListenerContainerFactory")
    // Bu sadece bir simülasyon, Spring'in internal event'i gibi tetiklenecek.
    @KafkaListener(topics = "payment.PaymentEvents", groupId = "order-service-group", containerFactory = "paymentKafkaListenerContainerFactory")
    public void listen(String message) {
        try {
            System.out.println("Received mock Kafka payment event: " + message);
            PaymentEvent event = objectMapper.readValue(message, PaymentEvent.class);
            handlePaymentEventUseCase.handlePaymentEvent(event);
        } catch (Exception e) {
            System.err.println("Error processing payment event: " + e.getMessage());
            // REQ-NON-003: Message processing errors, dead-letter queue here
            // Loglama, metriklere yansıtma ve/veya DLQ'ye gönderme.
        }
    }

    // Ödeme servisi mock event tetikleyicisi (TESTAMAÇLI)
    public void simulatePaymentEvent(PaymentEvent event) {
        try {
            String jsonEvent = objectMapper.writeValueAsString(event);
            listen(jsonEvent); // Directly call listener for simulation
        } catch (Exception e) {
            System.err.println("Failed to simulate payment event: " + e.getMessage());
        }
    }
}
