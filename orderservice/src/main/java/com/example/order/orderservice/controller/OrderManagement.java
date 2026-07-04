package com.example.order.orderservice.controller;

import com.example.order.orderservice.dto.OrderDto;
import com.example.order.orderservice.dto.OrderEvent;
import com.example.order.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order/")
@Slf4j
public class OrderManagement {

    private final OrderService service;

    public OrderManagement(OrderService service) {
        this.service = service;
    }

    @PostMapping("add")
    public ResponseEntity<?> addOrder(@RequestBody OrderDto orderDto)
    {
        Long userId = service.getUserId(orderDto.getEmail());
        Long restaurantId = service.getRestaurantId(orderDto.getEmail());
        if(userId==null || restaurantId==null) return ResponseEntity.badRequest().body("Please Login");

        orderDto.setUserId(userId);
        orderDto.setRestaurantId(restaurantId);

        OrderDto dto = service.save(orderDto);
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrderId(dto.getId());
        orderEvent.setUserId(dto.getUserId());
        orderEvent.setTotalAmount(dto.getTotalAmount());
        orderEvent.setRestaurantId(dto.getRestaurantId());
        orderEvent.setStatus("Accepted");
        log.info("Order event is " + orderEvent + "--" + dto);
        service.publishOrderPlaced(orderEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);

    }
}
