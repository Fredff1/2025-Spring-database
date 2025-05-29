package com.repairhub.management.vehicle.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.repairhub.management.auth.entity.User;
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
}
