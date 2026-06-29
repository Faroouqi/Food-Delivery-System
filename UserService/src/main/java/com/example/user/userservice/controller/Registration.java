package com.example.user.userservice.controller;

import com.example.user.userservice.dto.PasswordRequest;
import com.example.user.userservice.dto.UserRequestDTO;
import com.example.user.userservice.entity.User;
import com.example.user.userservice.service.UserService;
import com.example.user.userservice.utility.UserDetailUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class Registration {


    private final UserService userService;

    private final UserDetailUtil util;

    private final PasswordEncoder encoder;

    public Registration(UserService userService, UserDetailUtil util, PasswordEncoder encoder) {
        this.userService = userService;
        this.util = util;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registration(@Validated @RequestBody UserRequestDTO userRequestDTO) {

        if(userService.isEmailExist(userRequestDTO.getEmail()))
        {
            log.error("Email already exists: {}", userRequestDTO.getEmail());
            return errorMessage("Email already in use");
        }
        UserRequestDTO save = userService.save(userRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(save);
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@Validated @RequestBody PasswordRequest request)
    {
        User user = util.getUser();
        userService.updatePassword(request,user);
        return successfullyMessage("Password updated successfully");
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@Validated @RequestBody UserRequestDTO userDto)
    {
        if(util.getUser()==null) return errorMessage("Login Please");
        return ResponseEntity
                .ok(userService.save(userDto));
    }

    @PutMapping("reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String usermame,@RequestParam String pwd)
    {
        if(!userService.isEmailExist(usermame)) return errorMessage("User not found");

        UserRequestDTO userdto = userService.save(userService.reverserMap(userService.getUser(usermame)));
        return successfullyMessage("Password Reset Successfully");
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return ResponseEntity.ok("Logout successful");
    }

    @GetMapping("/profile")
    public ResponseEntity<UserRequestDTO> getProfile() {
        User user = util.getUser();
        return ResponseEntity.ok(userService.reverserMap(userService.getUser(user.getEmail())));
    }

    public ResponseEntity<?> errorMessage(String mess)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mess);
    }

    public ResponseEntity<?> successfullyMessage(String message)
    {
        return ResponseEntity.ok(message);
    }

    }
