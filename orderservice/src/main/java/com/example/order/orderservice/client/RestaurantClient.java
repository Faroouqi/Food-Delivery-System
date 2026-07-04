package com.example.order.orderservice.client;

import com.example.order.orderservice.config.FeignConfig;
import com.example.order.orderservice.dto.RestaurantDto;
import com.example.order.orderservice.dto.UserDto;
//import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "restaurantservice",
        url = "http://localhost:8091",
        configuration = FeignConfig.class
)
public interface RestaurantClient {

    @GetMapping("/api/restaurant/profile")
    RestaurantDto getRestaurantById(@RequestParam("email") String email);

}
