package com.example.order.order_service.application.port.out;

// EventPublisherPort.java

public interface EventPublisherPort {
    void publish(Object event); // Genel bir event gönderme arayüzü
}
