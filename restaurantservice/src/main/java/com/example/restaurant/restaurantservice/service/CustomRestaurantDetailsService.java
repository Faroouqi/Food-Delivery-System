package com.example.restaurant.restaurantservice.service;

import com.example.restaurant.restaurantservice.entity.Restaurant;
import com.example.restaurant.restaurantservice.respository.RestaurantRepository;
import com.example.restaurant.restaurantservice.utillity.CustomRestaurantDetail;
import com.example.restaurant.restaurantservice.utillity.RestaurantDetailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomRestaurantDetailsService implements UserDetailsService {
    @Autowired
    private final RestaurantRepository userRepository;

    public CustomRestaurantDetailsService(RestaurantRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Restaurant user = userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("Could not find User"));
        return new CustomRestaurantDetail(user);
    }
}
