package com.example.order.orderservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic orderPlacedTopic() {
        return TopicBuilder.name("order-placed-topic").partitions(3).replicas(1).build();
    }
    @Bean
    public NewTopic orderStatusTopic() {
        return TopicBuilder.name("order-status-events").partitions(3).replicas(1).build();
    }
}
