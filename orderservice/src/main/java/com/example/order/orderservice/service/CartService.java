package com.example.order.orderservice.service;

import com.example.order.orderservice.dto.CartDTO;
import com.example.order.orderservice.entity.Cart;
import com.example.order.orderservice.respository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CartService {
    public final CartRepository repository;

    public CartService(CartRepository repository) {
        this.repository = repository;
    }

    public CartDTO save(CartDTO dto)
    {
        List<Cart> cartItems = repository.findByUserId(dto.getUserId());

        if (!cartItems.isEmpty()) {

            Long restaurantId = cartItems.get(0).getRestaurantId();

            if (!restaurantId.equals(dto.getRestaurantId())) {
                throw new RuntimeException(
                        "You can order from only one restaurant at a time. Please clear your cart first."
                );
            }
        }
        Optional<Cart> check = repository.findByUserIdAndMenuId(dto.getUserId(), dto.getMenuId());
        if(check.isEmpty())
        {
            dto.setUpdatedAt(LocalDateTime.now());
            return reverseMap(repository.save(map(dto)));
        }else{
            Cart cart = check.get();
            cart.setQuantity(cart.getQuantity()+1);
            cart.setUpdatedAt(LocalDateTime.now());
            return reverseMap(repository.save(cart));
        }

    }
    @Transactional
    public CartDTO updateQuantity(Long id,Integer quantity)
    {
        Cart cart = repository.findById(id).get();
        cart.setQuantity(quantity);
        cart = repository.save(cart);
        return reverseMap(cart);
    }
    public Cart map(CartDTO dto) {

        return Cart.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .restaurantId(dto.getRestaurantId())
                .menuId(dto.getMenuId())
                .quantity(dto.getQuantity())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    public CartDTO reverseMap(Cart cart) {

        return CartDTO.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .restaurantId(cart.getRestaurantId())
                .menuId(cart.getMenuId())
                .quantity(cart.getQuantity())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }

    public List<CartDTO> getALl(Long id) {
        log.info("Second");
        List<Cart> carts = repository.findByUserId(id);
        log.info("Third");
        List<CartDTO> dtos = carts.stream()
                                .map(this::reverseMap)
                                .toList();
        return dtos;
    }

    @Transactional
    public boolean deleteQuantity(Long id) {
        repository.deleteById(id);
        return true;
    }

    @Transactional
    public boolean deleteAll(Long id) {
        repository.deleteByUserId(id);
        return true;
    }
}
