package com.example.restaurant.restaurantservice.controller;

import com.example.restaurant.restaurantservice.dto.MenuItemDTO;
import com.example.restaurant.restaurantservice.respository.MenuRepository;
import com.example.restaurant.restaurantservice.service.MenuService;
import com.example.restaurant.restaurantservice.utillity.CustomRestaurantDetail;
import com.example.restaurant.restaurantservice.utillity.RestaurantDetailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/menu")
public class Menu {

    private final RestaurantDetailUtil restaurantDetailUtil;

    private final MenuService service;

    @Autowired
    public Menu(RestaurantDetailUtil restaurantDetailUtil,
                MenuService service) {
        this.restaurantDetailUtil = restaurantDetailUtil;
        this.service = service;

    }

    @PostMapping("/add")
    public ResponseEntity<?> addMenu(@RequestBody MenuItemDTO menu) {

        if (!restaurantDetailUtil.isValidUser()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Login to Restaurant");
        }

        Long id = restaurantDetailUtil.getUser().getId();

        menu.setRestaurantId(id);

        MenuItemDTO dto = service.save(menu);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/menu/{id}")
    public ResponseEntity<?> updateMenu(@PathVariable Integer id,@RequestParam String field,
                                        @RequestParam String value)
    {
        log.info("Verifying");
        if(!restaurantDetailUtil.isValidUser()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not Authorized in");
        }

        log.info("inserting");
       MenuItemDTO dto = service.updateMenu(restaurantDetailUtil.getUser().getRestaurant(),field,value);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/characteristic")
    public List<String> getCharacteristic(@RequestParam String chara)
    {
        if(!restaurantDetailUtil.isValidUser()){
            return (List<String>) ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not Authorized in");
        }
        return service.getMenuOnCharacteristic(restaurantDetailUtil.getUser().getId().intValue(),chara);
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteMenu(@PathVariable Integer id)
    {
        service.deleteMenu(id);;
        log.info("Deleted Successfully");
        return ResponseEntity.ok("Deleted Successfully");
    }

}
