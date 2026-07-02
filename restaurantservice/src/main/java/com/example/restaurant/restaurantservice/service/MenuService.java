package com.example.restaurant.restaurantservice.service;

import com.example.restaurant.restaurantservice.controller.Menu;
import com.example.restaurant.restaurantservice.dto.MenuItemDTO;
import com.example.restaurant.restaurantservice.entity.MenuItem;
import com.example.restaurant.restaurantservice.entity.Restaurant;
import com.example.restaurant.restaurantservice.respository.MenuRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MenuService {
    private final MenuRepository repository;

    public MenuService(MenuRepository repository) {
        this.repository = repository;
    }
    public MenuItemDTO save(MenuItemDTO dto)
    {
        MenuItem menu  = repository.save(toEntity(dto));
        return toDto(menu);
    }

    public MenuItemDTO updateMenu(Restaurant restaurant,String field,String value)
    {
        MenuItem menuItem = repository.findById(restaurant.getId().intValue()).orElse(null);
        if(menuItem==null) {
            throw new RuntimeException("Menu is Not Found");
        }

        switch (field) {
            case "itemName" -> menuItem.setItemName(value);
            case "category" -> menuItem.setCategory(value);
            case "price" -> menuItem.setPrice(BigDecimal.valueOf(Integer.getInteger(value)));
            default -> throw new RuntimeException("Field not Found");
        }
        menuItem.setUpdatedAt(LocalDateTime.now());
        menuItem  = repository.save(menuItem);
        return toDto(menuItem);
    }

    public void deleteMenu(Integer id)
    {
        repository.deleteById(id);
    }

    public static MenuItem toEntity(MenuItemDTO dto) {
        if (dto == null) {
            return null;
        }
        MenuItem entity = new MenuItem();
        entity.setRestaurantId(dto.getRestaurantId());
        entity.setItemName(dto.getItemName());
        entity.setDescription(dto.getDescription());
        entity.setCategory(dto.getCategory());
        entity.setPrice(dto.getPrice());
        entity.setAvailable(dto.getIsAvailable());
        entity.setImageUrl(dto.getImageUrl());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }


    public static MenuItemDTO toDto(MenuItem entity) {
        if (entity == null) {
            return null;
        }
        MenuItemDTO dto = new MenuItemDTO();
        dto.setId(entity.getId());
        dto.setRestaurantId(entity.getRestaurantId());
        dto.setItemName(entity.getItemName());
        dto.setDescription(entity.getDescription());
        dto.setCategory(entity.getCategory());
        dto.setPrice(entity.getPrice());
        dto.setIsAvailable(entity.getAvailable());
        dto.setImageUrl(entity.getImageUrl());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
