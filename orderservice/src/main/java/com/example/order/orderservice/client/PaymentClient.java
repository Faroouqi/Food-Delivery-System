package com.example.order.orderservice.client;


import com.example.order.orderservice.config.FeignConfig;
import com.example.order.orderservice.dto.PaymentDTO;
import com.example.order.orderservice.dto.RestaurantDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "paymentservice",
        url = "http://localhost:8093",
        configuration = FeignConfig.class
)
public interface PaymentClient {

    @PostMapping("/payment/add")
    RestaurantDto addPayment(@RequestBody PaymentDTO paymentDTO);

}
