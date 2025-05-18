package com.repairhub.management.auth.service;

import org.springframework.stereotype.Service;

import com.repairhub.management.auth.dto.RegisterRequestDTO;
import com.repairhub.management.auth.entity.User;
import com.repairhub.management.auth.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(RegisterRequestDTO createUserDTO){
        User user = User.builder()
        .username(createUserDTO.getUsername())
        .password(createUserDTO.getPassword())
        .email(createUserDTO.getPassword())
        .status(createUserDTO.getUserStatus().name())
        .phone(createUserDTO.getPhone())
        .build();
        userRepository.insert(user);
        return user;
    }

}
