package com.example.payment.paymentservice.service;

import com.example.payment.paymentservice.dto.PaymentDTO;
import com.example.payment.paymentservice.entity.Payment;
import com.example.payment.paymentservice.repository.PaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class PaymentService {

@Autowired
private PaymentRepo repo;


public PaymentDTO save(PaymentDTO dto)
{
    return toDTO(repo.save(toEntity(dto)));
}
    public static PaymentDTO toDTO(Payment payment) {
        if (payment == null) {
            return null;
        }

        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrderId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setTransactionId(payment.getTransactionId());
        dto.setStatus(payment.getStatus());
        dto.setPaymentDate(payment.getPaymentDate());

        return dto;
    }

    // 2. Convert DTO to Entity (For saving incoming data to the database)
    public static Payment toEntity(PaymentDTO dto) {
        if (dto == null) {
            return null;
        }

        Payment payment = new Payment();
//        payment.setId(dto.getId());
        payment.setOrderId(dto.getOrderId());
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setTransactionId(dto.getTransactionId());
        payment.setStatus(dto.getStatus());
        payment.setPaymentDate(dto.getPaymentDate());

        return payment;
    }
}
