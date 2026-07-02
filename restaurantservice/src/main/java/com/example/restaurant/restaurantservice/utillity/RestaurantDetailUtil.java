package com.example.restaurant.restaurantservice.utillity;

import com.example.restaurant.restaurantservice.entity.Restaurant;
import com.example.restaurant.restaurantservice.service.RestaurantService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class RestaurantDetailUtil {

    private final RestaurantService restaurantService;

    public RestaurantDetailUtil(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    public String getUserName()
    {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        return null;
    }

    public CustomRestaurantDetail  getUser()
    {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal() instanceof CustomRestaurantDetail)) {
            return null;
        }

        return (CustomRestaurantDetail) authentication.getPrincipal();
    }
    public boolean isValidUser()
    {
        return getUser() != null;
    }
}
