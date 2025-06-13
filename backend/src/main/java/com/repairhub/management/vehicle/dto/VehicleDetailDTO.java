package com.repairhub.management.vehicle.dto;

import java.time.LocalDate;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.vehicle.entity.Vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleDetailDTO {
    private Long id;
    private String licensePlate;
    private String brand;
    private String model;
    private String ownerName;
    private LocalDate registerDate;

    public static VehicleDetailDTO from(Vehicle vehicle,User owner){
        VehicleDetailDTO dto = VehicleDetailDTO.builder()
        .id(vehicle.getVehicleId())
        .licensePlate(vehicle.getLicensePlate())
        .model(vehicle.getModel())
        .brand(vehicle.getBrand())
        .ownerName(owner.getUsername())
        .registerDate(vehicle.getRegisterDate()) 

        .build();
        return dto;
    }
}
