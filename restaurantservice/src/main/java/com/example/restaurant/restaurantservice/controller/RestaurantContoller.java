package com.example.restaurant.restaurantservice.controller;

import com.example.restaurant.restaurantservice.dto.RestaurantDTO;
import com.example.restaurant.restaurantservice.entity.Restaurant;
import com.example.restaurant.restaurantservice.service.RestaurantService;
import com.example.restaurant.restaurantservice.utillity.RestaurantDetailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/restaurant/")
public class RestaurantContoller {
    private final RestaurantService restaurantService;
    private final RestaurantDetailUtil restaurantDetailUtil;
    private final PasswordEncoder encoder;

    public RestaurantContoller(RestaurantService restaurantService, RestaurantDetailUtil restaurantDetailUtil, PasswordEncoder encoder) {
        this.restaurantService = restaurantService;
        this.restaurantDetailUtil = restaurantDetailUtil;
        this.encoder = encoder;
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

//    @GetMapping("test")
//    public String test() {
//
//        Restaurant restaurant =restaurantService.getUser("johnq1@example.com");
//
//        boolean matches = encoder.matches(
//                "NewPassword@123",
//                restaurant.getPassword()
//        );
//
//        return String.valueOf(matches);
//    }
    @GetMapping("profile")
    public ResponseEntity<?> getRestaurant(@RequestParam String email)
    {
        if(!restaurantService.isEmailExist(email)) return null;

        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.getUser(email));
    }
}
