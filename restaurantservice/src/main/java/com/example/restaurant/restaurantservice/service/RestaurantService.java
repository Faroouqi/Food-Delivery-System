package com.example.restaurant.restaurantservice.service;

import com.example.restaurant.restaurantservice.client.UserClient;
import com.example.restaurant.restaurantservice.dto.PasswordRequest;
import com.example.restaurant.restaurantservice.dto.RestaurantDTO;
import com.example.restaurant.restaurantservice.dto.UserDto;
import com.example.restaurant.restaurantservice.entity.Restaurant;
import com.example.restaurant.restaurantservice.respository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
public class RestaurantService {

    private final RestaurantRepository restaurantrepository;

    private final PasswordEncoder passwordEncoder;
    private final UserClient client;


    public RestaurantService(RestaurantRepository restaurantrepository, PasswordEncoder passwordEncoder, UserClient client) {
        this.restaurantrepository = restaurantrepository;
        this.passwordEncoder = passwordEncoder;
        this.client = client;
    }

    public Restaurant getUser(String email){
        return restaurantrepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Could not find User"));
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
//        UserDto dto = client.getUserById(userdto.getEmail());
//        log.info("dto is :" + dto);
        Restaurant user = new Restaurant();
        user.setEmail(userdto.getEmail());
        user.setOwnerId(Long.getLong("1"));
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
