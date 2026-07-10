package com.example.user.userservice.service;

import com.example.user.userservice.dto.OrderEvent;
import com.example.user.userservice.dto.PasswordRequest;
import com.example.user.userservice.dto.UserRequestDTO;
import com.example.user.userservice.entity.User;
import com.example.user.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
//@EnableKafka
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @KafkaListener(topics = "order-placed-topic", groupId = "user-notification-group")
    public void onOrderCreated(OrderEvent event) {
        System.out.println("=========================================");
        System.out.println("🎯 SUCCESS! Notify user " + event.getUserId() + ": order placed!");
        System.out.println("=========================================");
    }

    @KafkaListener(topics = "order-status-events", groupId = "user-notification-group")
    public void onStatusUpdate(OrderEvent event) {
        System.out.println("Order " + event.getOrderId() + " updated to " + event.getStatus());
    }



    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUser(String email){
        return userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Could not find User"));
    }

    public UserRequestDTO getUserDto(String email){
        return reverserMap(userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Could not find User")));
    }
    public boolean isEmailExist(String email)
    {
        return userRepository.existsByEmail(email);
    }
    public UserRequestDTO save(UserRequestDTO user)
    {
        User user1 = userRepository.save(map(user));
        log.info("Third");
        return reverserMap(user1);
    }

    public User map(UserRequestDTO userdto)
    {
        log.info("Second");
        User user = new User();
        user.setEmail(userdto.getEmail());
        user.setName(userdto.getUsername());
        user.setCreatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(userdto.getPassword()));
        user.setRole(userdto.getRole());
        user.setStatus("Available");
        user.setPhoneNumber(userdto.getPhoneNumber());
        user.setProfileImageUrl(userdto.getImage());
        user.setUpdateAt(LocalDateTime.now());

        return user;
    }

    public UserRequestDTO reverserMap(User user)
    {
        log.info("Fourth");
        UserRequestDTO userdto = new UserRequestDTO();
        userdto.setEmail(user.getEmail());
        userdto.setId(user.getId());
        userdto.setUsername(user.getName());
        userdto.setCreatedAt(LocalDateTime.now());
        userdto.setPassword(user.getPassword());
        userdto.setRole(user.getRole());
        userdto.setStatus(user.getStatus());
        userdto.setPhoneNumber(user.getPhoneNumber());
        userdto.setImage(user.getProfileImageUrl());
        userdto.setUpdateAt(LocalDateTime.now());

        return userdto;

    }
    public boolean passwordCheck(String oldPwd,User user)
    {
        return passwordEncoder.matches(oldPwd, user.getPassword());
    }

    public void updatePassword(PasswordRequest request, User user)
    {
        if(user==null) throw new RuntimeException("Login Please");
        if(!passwordCheck(request.getOldPassword(),user))
            throw new RuntimeException("Old password is incorrect");

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("New password cannot be the same as the old password");
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("New password cannot be the same as the old password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdateAt(LocalDateTime.now());
        userRepository.save(user);
    }
    public UserRequestDTO updateProfile(UserRequestDTO dto,User user)
    {

        user.setRole(dto.getRole());
        user.setEmail(dto.getEmail());
        user.setName(dto.getUsername());
        user.setPhoneNumber(dto.getPhoneNumber());
        User save = userRepository.save(user);
        return reverserMap(save);
    }
}
