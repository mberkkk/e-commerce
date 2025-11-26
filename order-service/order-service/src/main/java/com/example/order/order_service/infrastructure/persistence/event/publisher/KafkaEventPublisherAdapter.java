package com.example.order.order_service.infrastructure.persistence.event.publisher;

// KafkaEventPublisherAdapter.java

import com.example.order.order_service.application.dto.OrderCreatedEvent;
import com.example.order.order_service.application.port.out.EventPublisherPort;
import com.example.order.order_service.application.service.CancelOrderApplicationService;
import com.example.order.order_service.application.service.UpdateOrderStatusApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
// import org.springframework.kafka.core.KafkaTemplate; // Gerçek Kafka için
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaEventPublisherAdapter implements EventPublisherPort {
    private final ApplicationEventPublisher applicationEventPublisher; // Spring'in internal event mekanizması
    @Qualifier("realKafkaTemplate")
    private final KafkaTemplate<String, Object> kafkaTemplate;
    // private final KafkaTemplate<String, String> kafkaTemplate; // Gerçek Kafka
    // için

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public void publish(Object event) {
        applicationEventPublisher.publishEvent(event);
        System.out.println("Internal Spring Event Published: " + event.getClass().getSimpleName());

        try {
            String topic = getTopicForEvent(event);
            // POJO'yu doğrudan gönder!
            kafkaTemplate.send(topic, event);
            System.out.println("Event sent to Kafka");
        } catch (Exception e) {
            System.err.println("Failed to serialize or publish event to Kafka: " + e.getMessage());
        }
    }

    private String getTopicForEvent(Object event) {
        if (event instanceof OrderCreatedEvent) {
            return "ORDER_CREATED";
        } else if (event instanceof UpdateOrderStatusApplicationService.OrderStatusUpdatedEvent) {
            return "order-status-updated";
        } else if (event instanceof CancelOrderApplicationService.OrderCancelledEvent) {
            return "order-cancelled";
        }
        return "order-generic-event"; // Default topic
    }
}