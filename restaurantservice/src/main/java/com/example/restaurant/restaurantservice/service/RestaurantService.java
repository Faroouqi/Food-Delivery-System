package com.example.restaurant.restaurantservice.service;

import com.example.restaurant.restaurantservice.client.UserClient;
import com.example.restaurant.restaurantservice.dto.OrderEvent;
import com.example.restaurant.restaurantservice.dto.PasswordRequest;
import com.example.restaurant.restaurantservice.dto.RestaurantDTO;
import com.example.restaurant.restaurantservice.dto.UserDto;
import com.example.restaurant.restaurantservice.entity.Restaurant;
import com.example.restaurant.restaurantservice.respository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static java.lang.Thread.sleep;

@Service
@Slf4j
public class RestaurantService {

    private final RestaurantRepository restaurantrepository;

    private final PasswordEncoder passwordEncoder;
    private final UserClient client;

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    public RestaurantService(RestaurantRepository restaurantrepository, PasswordEncoder passwordEncoder, UserClient client, KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.restaurantrepository = restaurantrepository;
        this.passwordEncoder = passwordEncoder;
        this.client = client;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "order-placed-topic", groupId = "restaurant-service-group")
    public void onOrderCreated(OrderEvent event) {

        try {
            if(event.getStatus().equals("COMPLETED")) return;
            System.out.println("Restaurant received new order: " + event.getOrderId());
            publishStatus(event,"ACCEPTED");
            // Simulate restaurant processing

            Thread.sleep(10000);
            publishStatus(event,"PREPARING");

            Thread.sleep(10000);
            // Order accepted/completed
            publishStatus(event,"DELIVERED");



            System.out.println("Order completed: " + event.getOrderId());

        } catch (Exception e) {

            System.out.println("Failed to process order: " + event.getOrderId());

            // Notify Order Service about failure
            event.setStatus("FAILED");

            kafkaTemplate.send(
                    "order-status-events",
                    String.valueOf(event.getOrderId()),
                    event
            );

            e.printStackTrace();
        }
    }

    private void publishStatus(OrderEvent original, String status) {
        OrderEvent event = new OrderEvent();
        event.setOrderId(original.getOrderId());
        event.setUserId(original.getUserId());
        event.setRestaurantId(original.getRestaurantId());
        event.setTotalAmount(original.getTotalAmount());
        event.setStatus(status);

        kafkaTemplate.send(
                "order-status-events",
                String.valueOf(event.getOrderId()),
                event
        );
    }
    public RestaurantDTO getUser(String email){
        log.info("Email is :" + email);
        Restaurant save = restaurantrepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Could not find User"));
        return reverserMap(save);
    }
    public boolean isEmailExist(String email)
    {
        return restaurantrepository.existsByEmail(email);
    }
    public RestaurantDTO save(RestaurantDTO user)
    {
        Restaurant user1 = restaurantrepository.save(map(user));
        return reverserMap(user1);
    }

    public Restaurant map(RestaurantDTO userdto)
    {
        UserDto dto = client.getUserById(userdto.getEmail());
        log.info("id is : " + dto.getId());
        Restaurant user = new Restaurant();
        user.setEmail(userdto.getEmail());
        user.setOwnerId(dto.getId().longValue());
        user.setName(userdto.getName());
        user.setCreatedAt(LocalDateTime.now());
        System.out.println("Raw Password: " + userdto.getPassword());

        String hash = passwordEncoder.encode(userdto.getPassword());
        System.out.println("Encoded Password: " + hash);

        user.setPassword(hash);
        user.setStatus(userdto.getStatus());
        user.setPhoneNumber(userdto.getPhone_number());
        user.setUpdatedAt(LocalDateTime.now());
        user.setAddress(userdto.getAddress());
        user.setClosingTime(userdto.getClosingTime());
        user.setCuisine(userdto.getCuisine());
        user.setOpeningTime(userdto.getOpeningTime());
        user.setRating(BigDecimal.ZERO);
        user.setStatus("Available");

        return user;
    }

    public RestaurantDTO reverserMap(Restaurant user)
    {
        RestaurantDTO userdto = new RestaurantDTO();
        userdto.setId(user.getId());
        userdto.setEmail(user.getEmail());
        userdto.setName(user.getName());
//        userdto.setPassword(passwordEncoder.encode(user.getPassword()));
        userdto.setStatus(user.getStatus());
        userdto.setPhone_number(user.getPhoneNumber());
        userdto.setOpeningTime(user.getOpeningTime());
        userdto.setClosingTime(user.getClosingTime());
        userdto.setCuisine(user.getCuisine());
        userdto.setAddress(user.getAddress());
        userdto.setOwnerId(user.getOwnerId());


        return userdto;

    }
    public boolean passwordCheck(String oldPwd,Restaurant user)
    {
        return passwordEncoder.matches(oldPwd, user.getPassword());
    }

    public void updatePassword(PasswordRequest request, Restaurant user)
    {
        if(user==null) throw new RuntimeException("Login Please");
        if(!passwordCheck(request.getOldPassword(),user))
            throw new RuntimeException("Old password is incorrect");

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("New password cannot be the same as the old password");
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("New password cannot be the same as the old password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(
                LocalDateTime.now());
        save(reverserMap(user));
    }
}
