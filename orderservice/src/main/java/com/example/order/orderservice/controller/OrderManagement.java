package com.example.order.orderservice.controller;

import com.example.order.orderservice.client.PaymentClient;
import com.example.order.orderservice.dto.OrderDto;
import com.example.order.orderservice.dto.OrderEvent;
import com.example.order.orderservice.dto.PaymentDTO;
import com.example.order.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("order/")
@Slf4j
public class OrderManagement {

    private final OrderService service;
    private final PaymentClient paymentClient;


    public OrderManagement(OrderService service, PaymentClient paymentClient) {
        this.service = service;
        this.paymentClient = paymentClient;
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

        orderEvent.setStatus("Pending");
        log.info("Order event is " + orderEvent + "--" + dto);
        PaymentDTO dto1 = getDto(orderDto.getTotalAmount(),"Card");
        dto1.setOrderId(dto.getId());
        System.out.println("Dto is :" + dto1);
        paymentClient.addPayment(dto1);
        service.publishOrderPlaced(orderEvent,dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);

    }
    @GetMapping("view/{id}")
    public ResponseEntity<?> viewOrder(@RequestParam Long id)
    {
        return ResponseEntity.ok(service.getOrders(id));
    }

    public PaymentDTO getDto(BigDecimal amount,String paymentMethod)
    {
        PaymentDTO dto = new PaymentDTO();
        dto.setAmount(amount);
        dto.setPaymentDate(LocalDateTime.now());
        dto.setStatus("COMPLETED");

        return dto;

    }


}
