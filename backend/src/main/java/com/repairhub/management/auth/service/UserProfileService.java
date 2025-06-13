package com.repairhub.management.auth.service;

import org.springframework.stereotype.Service;

import com.repairhub.management.auth.repository.UserRepository;
import com.repairhub.management.auth.dto.UserStatusDTO;

@Service
public class UserProfileService {
    private final UserRepository userRepository;

    public UserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserStatusDTO updateStatus(Long userId,UserStatusDTO userStatusDTO){
        userRepository.updateStatus(userId, userStatusDTO.getStatus());
        return userStatusDTO;
    }
}
