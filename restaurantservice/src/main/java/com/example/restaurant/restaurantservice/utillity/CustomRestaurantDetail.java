package com.example.restaurant.restaurantservice.utillity;

import com.example.restaurant.restaurantservice.entity.Restaurant;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomRestaurantDetail implements UserDetails {
    private final Restaurant restaurant;

    public CustomRestaurantDetail(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return restaurant.getPassword();
    }

    @Override
    public String getUsername() {
        return restaurant.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId()
    {
        return restaurant.getId();
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
}
