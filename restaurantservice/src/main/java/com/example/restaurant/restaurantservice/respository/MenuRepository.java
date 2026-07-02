package com.example.restaurant.restaurantservice.respository;

import com.example.restaurant.restaurantservice.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<MenuItem,Integer> {

}
