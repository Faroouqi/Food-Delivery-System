package com.example.restaurant.restaurantservice.client;

import com.example.restaurant.restaurantservice.config.FeignConfig;
import com.example.restaurant.restaurantservice.dto.UserDto;
//import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "userservice",
        url = "http://localhost:8090",
        configuration = FeignConfig.class
)
public interface UserClient {

    @GetMapping("/api/users/email")
    UserDto getUserById(@RequestParam("email") String email);
}