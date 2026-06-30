package com.example.restaurant.restaurantservice.respository;

import com.example.restaurant.restaurantservice.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant,Integer> {

    Optional<Restaurant> findByEmail(String email);

    boolean existsByEmail(String email);
}
