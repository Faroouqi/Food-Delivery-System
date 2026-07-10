package com.example.payment.paymentservice.controller;

import com.example.payment.paymentservice.dto.PaymentDTO;
import com.example.payment.paymentservice.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("payment/")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService service;
    @PostMapping("add")
    public ResponseEntity<?> addPayment(@Validated @RequestBody PaymentDTO dto)
    {
        log.info("Adding payment for order id: "+ dto.getOrderId());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }
}
