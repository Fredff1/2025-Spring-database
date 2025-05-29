package com.repairhub.management.user.service;

import org.springframework.stereotype.Service;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.order.repository.RepairOrderRepository;
import com.repairhub.management.repair.repository.RepairRecordRepository;
import com.repairhub.management.user.dto.UserStatusDTO;
import com.repairhub.management.vehicle.repository.VehicleRepository;

@Service
public class UserInfoService {
    private final VehicleRepository vehicleRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final RepairRecordRepository recordRepository;

    public UserInfoService(
        VehicleRepository vehicleRepository,
        RepairOrderRepository repairOrderRepository,
        RepairRecordRepository recordRepository
    ){
        this.vehicleRepository = vehicleRepository;
        this.repairOrderRepository = repairOrderRepository;
        this.recordRepository = recordRepository;
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
}
