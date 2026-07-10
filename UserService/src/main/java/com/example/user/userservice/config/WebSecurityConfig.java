package com.example.user.userservice.config;

import com.example.user.userservice.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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

    @Value("${internal.api.key}")
    private String internalApiKey;
    private final CustomUserDetailsService customUserDetails;

    @Autowired
    private CustomAuthenticationSucccessHandler customAuthenticationSucccessHandler;

    private final String[] publicUrl = {
            "/", "/api/users/register", "/login", "/logout", "/home", "/send-otp", "/verify-otp", "/reset-password"
    };

    @Autowired
    public WebSecurityConfig(CustomUserDetailsService customUserDetails) {
        this.customUserDetails = customUserDetails;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
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
                        // 1. Specific API Authorization (Allows authenticated user OR valid API key)
                        .requestMatchers("/api/users/email").access((authentication, context) -> {
                            // Check if the current user is fully authenticated (logged in)
                            boolean isUserLoggedIn = authentication.get().isAuthenticated()
                                    && !(authentication.get() instanceof AnonymousAuthenticationToken);

                            // Check if the request contains the valid service-to-service secret API key
                            String providedKey = context.getRequest().getHeader("X-Internal-Api-Key");
                            boolean isValidApiKey = internalApiKey != null && internalApiKey.equals(providedKey);

                            // Grant access if either condition is met
                            return new AuthorizationDecision(isUserLoggedIn || isValidApiKey);
                        })
                        // 2. Public endpoints
                        .requestMatchers(publicUrl).permitAll()
                        // 3. Fallback for all other endpoints
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(customAuthenticationSucccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((req, res, auth) ->
                                res.setStatus(HttpServletResponse.SC_OK))
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, authException) ->
                                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetails);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
