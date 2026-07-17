package com.example.order.orderservice.respository;

import com.example.order.orderservice.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByUserId(Long userId);

    Optional<Cart> findByUserIdAndMenuId(Long userId, Long menuId);

    void deleteByUserId(Long userId);

    boolean existsByUserIdAndRestaurantId(Long userId, Long restaurantId);
}