package com.example.order.orderservice.respository;

import com.example.order.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {

    @Query("SELECT t FROM Order t WHERE t.userId = :userId")
    List<Order> findByUserId(@Param("userId") Long userId);


    @Query("SELECT t FROM Order t WHERE t.restaurantId = :restaurantId")
    List<Order> findByRestaurnatId(@Param("restaurantId") Long restaurantId);
}
