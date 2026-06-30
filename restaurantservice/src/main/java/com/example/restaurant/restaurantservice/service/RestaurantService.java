package com.example.restaurant.restaurantservice.service;

import com.example.restaurant.restaurantservice.dto.PasswordRequest;
import com.example.restaurant.restaurantservice.dto.RestaurantDTO;
import com.example.restaurant.restaurantservice.entity.Restaurant;
import com.example.restaurant.restaurantservice.respository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantrepository;

    private final PasswordEncoder passwordEncoder;


    public RestaurantService(RestaurantRepository restaurantrepository, PasswordEncoder passwordEncoder) {
        this.restaurantrepository = restaurantrepository;
        this.passwordEncoder = passwordEncoder;
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
        Restaurant user = new Restaurant();
        user.setEmail(userdto.getEmail());
        user.setName(userdto.getName());
        user.setCreatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(userdto.getPassword()));
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
        userdto.setPassword(passwordEncoder.encode(user.getPassword()));
        userdto.setStatus(user.getStatus());
        userdto.setPhone_number(user.getPhoneNumber());

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
