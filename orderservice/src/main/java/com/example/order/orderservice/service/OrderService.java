package com.example.order.orderservice.service;

import com.example.order.orderservice.client.RestaurantClient;
import com.example.order.orderservice.client.UserClient;
import com.example.order.orderservice.dto.OrderDto;
import com.example.order.orderservice.dto.OrderEvent;
import com.example.order.orderservice.entity.Order;
import com.example.order.orderservice.respository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {
    private final UserClient userClient;
    private final RestaurantClient restaurantClient;
    private final OrderRepository repository;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;


    @Autowired
    public OrderService(UserClient userClient, RestaurantClient restaurantClient, OrderRepository repository, KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.userClient = userClient;
        this.restaurantClient = restaurantClient;
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderPlaced(OrderEvent event) {
        log.info("Publishing order to Kafka: {}", event.getOrderId());
        String partitionKey = String.valueOf(event.getOrderId());
        kafkaTemplate.send("order-placed-topic", partitionKey, event);
    }

    @KafkaListener(topics = "order-status-events", groupId = "order-service-group")
    public void onStatusUpdate(OrderEvent event) {
        System.out.println("Order " + event.getOrderId() + " updated to " + event.getStatus());

        // update DB status here
    }
    public Long getUserId(String email)
    {
        return userClient.getUserById(email).getId().longValue();
    }

    public Long getRestaurantId(String email)
    {
        return restaurantClient.getRestaurantById(email).getId();
    }
    public OrderDto save(OrderDto dto)
    {
        Order save = repository.save(toEntity(dto));
        return toDto(save);
    }


    public static OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }

        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setRestaurantId(order.getRestaurantId());
        dto.setPaymentId(order.getPaymentId());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setOrderDate(order.getOrderDate());
        dto.setUpdatedAt(order.getUpdatedAt());
        return dto;
    }

    public static Order toEntity(OrderDto dto) {
        if (dto == null) {
            return null;
        }

        Order order = new Order();
        order.setId(dto.getId());
        order.setUserId(dto.getUserId());
        order.setRestaurantId(dto.getRestaurantId());
        order.setPaymentId(dto.getPaymentId());
        order.setStatus(dto.getStatus());
        order.setTotalAmount(dto.getTotalAmount());
        order.setDeliveryAddress(dto.getDeliveryAddress());
        order.setOrderDate(dto.getOrderDate());
        order.setUpdatedAt(dto.getUpdatedAt());
        return order;
    }
}
