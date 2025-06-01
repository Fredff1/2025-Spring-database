package com.repairhub.management.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.repairhub.management.admin.dto.AdminIncomeDTO;
import com.repairhub.management.auth.repository.UserRepository;
import com.repairhub.management.repair.dto.LaborFeeLogDTO;
import com.repairhub.management.repair.entity.LaborFeeLog;
import com.repairhub.management.repair.repository.LaborFeeLogRepository;
import com.repairhub.management.repairman.repository.RepairmanProfileRepository;
import com.repairhub.management.utils.PageUtils;
import com.repairhub.management.vehicle.dto.VehicleDTO;
import com.repairhub.management.vehicle.dto.VehicleDetailDTO;
import com.repairhub.management.vehicle.entity.Vehicle;
import com.repairhub.management.vehicle.repository.VehicleRepository;

@Service
public class AdminService {
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final LaborFeeLogRepository laborFeeLogRepository;
    private final RepairmanProfileRepository profileRepository;

    public AdminService(
        VehicleRepository vehicleRepository,
        UserRepository userRepository,
        LaborFeeLogRepository laborFeeLogRepository,
        RepairmanProfileRepository profileRepository
    ) {
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.laborFeeLogRepository = laborFeeLogRepository;
        this.profileRepository = profileRepository;
    }

    public List<VehicleDetailDTO> findAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<VehicleDetailDTO> dtos = vehicles.stream()
        .map(vehicle -> VehicleDetailDTO.from(vehicle,userRepository.findById(vehicle.getOwnerId()).get()))
        .collect(Collectors.toList());
        return dtos;
    }

    public AdminIncomeDTO getIncome(
        int page,
        int size
    ){
        List<LaborFeeLog> logs = laborFeeLogRepository.findAll();
        List<LaborFeeLog> pagedLogs = PageUtils.paginate(logs, page, size);
        List<LaborFeeLogDTO> dtos = pagedLogs.stream()
        .map(log -> LaborFeeLogDTO.from(
            log, 
            profileRepository.findByUserId(log.getRepairmanId()).get(),
            userRepository.findById(log.getRepairmanId()).get()))
        .collect(Collectors.toList());
        AdminIncomeDTO incomeDTO = AdminIncomeDTO.builder()
        .list(dtos)
        .total(logs.size())
        .build();
        return incomeDTO;
    }
}
