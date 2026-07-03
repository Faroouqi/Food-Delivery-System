package com.example.restaurant.restaurantservice.service;

import com.example.restaurant.restaurantservice.entity.Restaurant;
import com.example.restaurant.restaurantservice.respository.RestaurantRepository;
import com.example.restaurant.restaurantservice.utillity.CustomRestaurantDetail;
import com.example.restaurant.restaurantservice.utillity.RestaurantDetailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomRestaurantDetailsService implements UserDetailsService {
    @Autowired
    private final RestaurantRepository userRepository;

    public CustomRestaurantDetailsService(RestaurantRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        Restaurant user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find User"));

        System.out.println("Stored Password : " + user.getPassword());

        return new CustomRestaurantDetail(user);
    }
}
//'$2a$10$tsS9uWyDkey50YFqh3TS9ek0xzOr2aMD7m8iwnmLahtUcy/Csy5ui'
