package com.repairhub.management.vehicle.repository;

import java.util.Optional;

import com.repairhub.management.vehicle.entity.Vehicle;

public interface VehicleRepository {
    public int insert(Vehicle vehicle);
    public Optional<Vehicle> findById(Long vehicleId);
    public Optional<Vehicle> findByOwnerId(Long ownerId);
}
