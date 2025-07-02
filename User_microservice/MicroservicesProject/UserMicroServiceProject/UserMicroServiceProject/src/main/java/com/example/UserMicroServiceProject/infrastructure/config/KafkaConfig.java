package com.example.UserMicroServiceProject.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    // Topics for User Service to publish
    @Bean
    public NewTopic userRegisteredTopic() {
        return TopicBuilder.name("user-registered")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic userProfileUpdatedTopic() {
        return TopicBuilder.name("USER_PROFILE_UPDATED")
                .partitions(3)
                .replicas(1)
                .build();
    }

    // Topics for User Service to consume
    @Bean
    public NewTopic orderCreatedTopic() {
        return TopicBuilder.name("ORDER_CREATED")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic productPriceChangedTopic() {
        return TopicBuilder.name("PRODUCT_PRICE_CHANGED")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic productOutOfStockTopic() {
        return TopicBuilder.name("PRODUCT_OUT_OF_STOCK")
                .partitions(3)
                .replicas(1)
                .build();
    }
}