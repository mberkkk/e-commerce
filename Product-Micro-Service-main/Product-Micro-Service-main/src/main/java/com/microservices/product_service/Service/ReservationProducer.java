package com.microservices.product_service.Service;

import com.microservices.product_service.Response.EventResponse;
import com.microservices.product_service.Response.ReserveStockResponse;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationProducer {
    private final NewTopic topic;
    private final KafkaTemplate<String, EventResponse> kafkaTemplate;

    public void sendMessage(EventResponse event) {
        System.out.println("Mesaj gonderildi");
        Message<EventResponse> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, topic.name())
                .build();

        kafkaTemplate.send(message);
    }
}
