package com.example.restaurant.restaurantservice.controller;

import com.example.restaurant.restaurantservice.dto.RestaurantDTO;
import com.example.restaurant.restaurantservice.service.RestaurantService;
import com.example.restaurant.restaurantservice.utillity.RestaurantDetailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/restaurant/")
public class Restaurant {
    private final RestaurantService restaurantService;
    private final RestaurantDetailUtil restaurantDetailUtil;

    public Restaurant(RestaurantService restaurantService, RestaurantDetailUtil restaurantDetailUtil) {
        this.restaurantService = restaurantService;
        this.restaurantDetailUtil = restaurantDetailUtil;
    }

    @PostMapping("register")
    public ResponseEntity<?> registration(@RequestBody RestaurantDTO dto)
    {
//        if(!restaurantDetailUtil.isValidUser()) return ResponseEntity.badRequest().body("Please Login");
        if(restaurantService.isEmailExist(dto.getEmail())) return ResponseEntity.badRequest().body("Account exist with " + dto.getEmail() + " . Please Login with this email.");
        RestaurantDTO restaurantDTO  = restaurantService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantDTO);
    }

    @PutMapping("update")
    public ResponseEntity<?> updateRestaurant(@RequestBody RestaurantDTO dto)
    {
        if(!restaurantDetailUtil.isValidUser())
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not Authorized in");
        }
        return ResponseEntity.ok(restaurantService.save(dto));
    }


}
