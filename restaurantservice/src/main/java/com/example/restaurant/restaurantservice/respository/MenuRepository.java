package com.example.restaurant.restaurantservice.respository;

import com.example.restaurant.restaurantservice.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<MenuItem,Integer> {

    @Query("SELECT t.category FROM MenuItem t " +
            "WHERE t.restaurantId = :restaurantId " +
            "AND t.category = :category ")
    List<String> findByRestaurantId(@Param("restaurantId") Integer restaurantId, @Param("category") String category);
}
