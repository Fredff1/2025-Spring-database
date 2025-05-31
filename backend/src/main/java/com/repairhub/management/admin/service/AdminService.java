package com.repairhub.management.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.repairhub.management.auth.repository.UserRepository;
import com.repairhub.management.vehicle.dto.VehicleDTO;
import com.repairhub.management.vehicle.dto.VehicleDetailDTO;
import com.repairhub.management.vehicle.entity.Vehicle;
import com.repairhub.management.vehicle.repository.VehicleRepository;

@Service
public class AdminService {
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public AdminService(
        VehicleRepository vehicleRepository,
        UserRepository userRepository
    ) {
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    public List<VehicleDetailDTO> findAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<VehicleDetailDTO> dtos = vehicles.stream()
        .map(vehicle -> VehicleDetailDTO.from(vehicle,userRepository.findById(vehicle.getOwnerId()).get()))
        .collect(Collectors.toList());
        return dtos;
    }
}
