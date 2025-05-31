package com.repairhub.management.vehicle.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.vehicle.dto.CreateVehicleRequest;
import com.repairhub.management.vehicle.dto.UpdateVehicleRequest;
import com.repairhub.management.vehicle.entity.Vehicle;
import com.repairhub.management.vehicle.repository.VehicleRepository;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(
        VehicleRepository vehicleRepository
    ){
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> getVehicles(User user){
        List<Vehicle> vehicles = vehicleRepository.findByOwner(user.getUserId());
        return vehicles;
    }

    public void deleteVehicle(Long vehicleId, User user) {
        vehicleRepository.deleteById(vehicleId);    
    }

    public void updateVehicle(UpdateVehicleRequest request,Long vehicleId){
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
        .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicleRepository.update(vehicle);
    }

    public Vehicle createVehicle(CreateVehicleRequest request, User user) {
       Vehicle vehicle = Vehicle.builder()
       .ownerId(user.getUserId())
       .brand(request.getBrand())
       .model(request.getModel())
       .licensePlate(request.getLicensePlate())
       .registerDate(LocalDate.now())
       .build();
       vehicleRepository.insert(vehicle);
       return vehicle;
    }
}
