package com.repairhub.management.user.service;

import org.springframework.stereotype.Service;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.auth.repository.UserRepository;
import com.repairhub.management.order.repository.RepairOrderRepository;
import com.repairhub.management.repair.repository.RepairRecordRepository;
import com.repairhub.management.user.dto.UserProfileDTO;
import com.repairhub.management.user.dto.UserProfileUpdateDTO;
import com.repairhub.management.user.dto.UserStatusDTO;
import com.repairhub.management.vehicle.repository.VehicleRepository;

@Service
public class UserInfoService {
    private final VehicleRepository vehicleRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final RepairRecordRepository recordRepository;
    private final UserRepository userRepository;

    public UserInfoService(
        VehicleRepository vehicleRepository,
        RepairOrderRepository repairOrderRepository,
        RepairRecordRepository recordRepository,
        UserRepository userRepository
    ){
        this.vehicleRepository = vehicleRepository;
        this.repairOrderRepository = repairOrderRepository;
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
    }

    public UserStatusDTO getStatus(User user){
        Long userId = user.getUserId();
        UserStatusDTO statusDTO = UserStatusDTO.builder()
        .historyCount(recordRepository.countByUserId(userId))
        .vehicleCount(vehicleRepository.countByUserId(userId))
        .orderCount(repairOrderRepository.countByUserId(userId))
        .build();
        return statusDTO;
    }

    public UserProfileDTO getProfile(User user){
        UserProfileDTO dto = UserProfileDTO.builder()
        .id(user.getUserId())
        .username(user.getUsername())
        .phone(user.getPhone())
        .email(user.getEmail())
        .createTime(user.getCreatedAt())
        .updateTime(user.getUpdatedAt())
        .userStatus(user.getStatus())
        .build();
        return dto;
    }

    public UserProfileDTO updateProfile(User user, UserProfileUpdateDTO profile){
        String newUsername = profile.getUsername();
        String newPhone = profile.getPhone();
        String newEmail = profile.getEmail();
        userRepository.updateBasicInfo(user.getUserId(), newUsername, newPhone, newEmail);

        User updatedUser = userRepository.findById(user.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        return getProfile(updatedUser);
    }
}
