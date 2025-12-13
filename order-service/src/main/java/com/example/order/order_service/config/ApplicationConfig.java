package com.example.order.order_service.config;

import com.example.order.order_service.application.port.in.*;
import com.example.order.order_service.application.port.out.OrderRepositoryPort;
import com.example.order.order_service.application.port.out.CartServiceClientPort;
import com.example.order.order_service.application.port.out.ProductServiceClientPort;
import com.example.order.order_service.application.port.out.PaymentServiceClientPort;
import com.example.order.order_service.application.port.out.EventPublisherPort;
import com.example.order.order_service.application.port.out.UserServiceClientPort;

import com.example.order.order_service.application.service.CreateOrderApplicationService;
import com.example.order.order_service.application.service.GetOrderDetailsApplicationService;
import com.example.order.order_service.application.service.ListCustomerOrdersApplicationService;
import com.example.order.order_service.application.service.CancelOrderApplicationService;
import com.example.order.order_service.application.service.PaymentEventHandlerApplicationService;
import com.example.order.order_service.application.service.UpdateOrderStatusApplicationService;

import com.example.order.order_service.infrastructure.persistence.client.CartServiceRestAdapter;
import com.example.order.order_service.infrastructure.persistence.client.PaymentServiceMockClientAdapter;
import com.example.order.order_service.infrastructure.persistence.client.ProductServiceRestAdapter;
import com.example.order.order_service.infrastructure.persistence.client.UserServiceRestAdapter;
import com.example.order.order_service.infrastructure.persistence.event.publisher.KafkaEventPublisherAdapter;
import com.example.order.order_service.infrastructure.persistence.jpa.adapter.SpringDataOrderRepositoryAdapter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class ApplicationConfig {

    // ======================== Use Case Beans ========================
    @Bean
    @Primary
    public CreateOrderPort createOrderPort(
            OrderRepositoryPort orderRepository,
            CartServiceClientPort cartService,
            ProductServiceClientPort productService,
            UserServiceClientPort userService,
            EventPublisherPort eventPublisher) {
        return new CreateOrderApplicationService(
                orderRepository,
                cartService,
                productService,
                userService,
                eventPublisher);
    }

    @Bean
    @Primary
    public GetOrderDetailsPort getOrderDetailsPort(OrderRepositoryPort orderRepository) {
        return new GetOrderDetailsApplicationService(orderRepository);
    }

    @Bean
    @Primary
    public ListCustomerOrdersPort listCustomerOrdersPort(OrderRepositoryPort orderRepository) {
        return new ListCustomerOrdersApplicationService(orderRepository);
    }

    @Bean
    @Primary
    public UpdateOrderStatusPort updateOrderStatusPort(OrderRepositoryPort orderRepository,
            EventPublisherPort eventPublisher) {
        return new UpdateOrderStatusApplicationService(orderRepository, eventPublisher);
    }

    @Bean
    @Primary
    public CancelOrderPort cancelOrderPort(OrderRepositoryPort orderRepository, ProductServiceClientPort productService,
            EventPublisherPort eventPublisher) {
        return new CancelOrderApplicationService(orderRepository, productService, eventPublisher);
    }

    @Bean
    @Primary
    public HandlePaymentEventPort handlePaymentEventPort(OrderRepositoryPort orderRepository,
            EventPublisherPort eventPublisher) {
        return new PaymentEventHandlerApplicationService(orderRepository, eventPublisher);
    }

    // ======================== Output Port Adapters ========================
    @Bean
    @Primary
    public OrderRepositoryPort orderRepositoryPort(SpringDataOrderRepositoryAdapter adapter) {
        return adapter;
    }

    @Bean
    @Primary
    public UserServiceClientPort userServiceClientPort(UserServiceRestAdapter adapter) {
        return adapter;
    }

    // 🎯 Cart Service Client - Spring profiles ile otomatik seçim yapılacak
    // @Profile("mock") → CartServiceRestAdapter
    // @Profile("!mock") → CartServiceFeignAdapter

    // 🎯 Product Service Client - Spring profiles ile otomatik seçim yapılacak
    // @Profile("mock") → ProductServiceRestAdapter
    // @Profile("!mock") → ProductServiceFeignAdapter

    @Bean
    @Primary
    public PaymentServiceClientPort paymentServiceClientPort(PaymentServiceMockClientAdapter adapter) {
        return adapter;
    }

    @Bean
    @Primary
    public EventPublisherPort eventPublisherPort(ApplicationEventPublisher applicationEventPublisher, KafkaTemplate<String, Object> kafkaTemplate) {
        return new KafkaEventPublisherAdapter(applicationEventPublisher, kafkaTemplate);
    }

    // ======================== Kafka Configuration ========================
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> paymentKafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}