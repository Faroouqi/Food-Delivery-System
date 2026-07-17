package com.example.order.orderservice.controller;

import com.example.order.orderservice.dto.CartDTO;
import com.example.order.orderservice.entity.Cart;
import com.example.order.orderservice.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/cart")
public class CartController {
    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItems(@RequestBody CartDTO dto)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getItems(@PathVariable Long id)
    {
        log.info("First");
        List<CartDTO> cartDTOList = service.getALl(id);

        return ResponseEntity.ok(cartDTOList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id,@RequestParam Integer quantity)
    {
        return ResponseEntity.ok(service.updateQuantity(id,quantity));
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id)
    {
        return ResponseEntity.ok(service.deleteQuantity(id));
    }

    @DeleteMapping("/remove/all")
    public ResponseEntity<?> deleteAll(@RequestParam Long id)
    {
        return ResponseEntity.ok(service.deleteAll(id));
    }


}
