package com.example.restaurant.restaurantservice.config;


import com.example.restaurant.restaurantservice.service.CustomRestaurantDetailsService;
import com.example.restaurant.restaurantservice.utillity.CustomRestaurantDetail;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class WebSecurityConfig {
    @Autowired
    private  CustomRestaurantDetailsService customUserDetails;


    @Value("${internal.api.key}")
    private String internalApiKey;
    @Autowired
    private CustomAuthenticationSucccessHandler customAuthenticationSucccessHandler;

    private final String[] publicUrl = {
            "/", "/api/restaurant/register","/api/restaurant/test", "/login", "/logout", "/home","/send-otp", "/verify-otp","/reset-password"
    };

    @Autowired
    public WebSecurityConfig(CustomRestaurantDetailsService customUserDetails) {
        this.customUserDetails = customUserDetails;
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:5173")); // React frontend
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true); // Important for session/cookies
                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/restaurant/profile").access((authentication, context) -> {
                            String providedKey = context.getRequest().getHeader("X-Internal-Api-Key");
                            boolean valid = internalApiKey.equals(providedKey);
                            return new AuthorizationDecision(valid);
                        })
                        .requestMatchers(publicUrl).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .successHandler(customAuthenticationSucccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((req, res, auth) ->
                                res.setStatus(HttpServletResponse.SC_OK))
                )
                // .httpBasic(Customizer.withDefaults())  ← 🗑️ remove this line
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, authException) ->
                                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        System.out.println("First");
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(customUserDetails);
        System.out.println("Second");
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
