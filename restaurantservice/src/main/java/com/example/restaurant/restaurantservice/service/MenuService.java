package com.example.restaurant.restaurantservice.service;

import com.example.restaurant.restaurantservice.controller.Menu;
import com.example.restaurant.restaurantservice.dto.MenuItemDTO;
import com.example.restaurant.restaurantservice.entity.MenuItem;
import com.example.restaurant.restaurantservice.entity.Restaurant;
import com.example.restaurant.restaurantservice.respository.MenuRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
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
    public List<String> getMenuOnCharacteristic(Integer id,String chara)
    {
        log.info("Resturant id is: "+id+" and category is: "+chara);
        List<String> menuItem = repository.findByRestaurantId(id,chara);
        if(menuItem.isEmpty())
        {
            return new ArrayList<>();
        }
        return menuItem;
    }
    public List<MenuItemDTO> getBasedOnId(Integer id)
    {
        List<MenuItem> menuList = repository.findByRestId(id);
        return menuList.stream().map(this::toDto).toList();
    }
    public MenuItemDTO getMenuBasedOnId(Integer id)
    {
        Optional<MenuItem> menuList = repository.findById(id);
        return toDto(menuList.get());
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


    public MenuItemDTO toDto(MenuItem entity) {
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
