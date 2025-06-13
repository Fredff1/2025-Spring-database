package com.repairhub.management.vehicle.repository;

import java.util.List;
import java.util.Optional;

import com.repairhub.management.common.dto.PageResponse;
import com.repairhub.management.vehicle.entity.Vehicle;

public interface VehicleRepository {

    int insert(Vehicle v);

    int update(Vehicle v);

    int deleteById(Long vehicleId);

    Optional<Vehicle> findById(Long vehicleId);

    List<Vehicle> findByOwner(Long ownerId);

    List<Vehicle> findAll();
    
    PageResponse<Vehicle> findAllWithPage(int pageNum,int pageSize);

    List<Vehicle> findByBrand(String brand);

    List<Vehicle> findByModel(String model);

    Optional<Vehicle> findByLicensePlate(String licensePlate);

    int countByUserId(Long userId);
}
