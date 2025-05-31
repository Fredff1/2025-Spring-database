package com.repairhub.management.vehicle.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.repairhub.management.vehicle.entity.Vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleDTO {
    private Long id;
    private String licensePlate;
    private String brand;
    private String model;
    private String vin;
    private LocalDate registerDate;

    public static VehicleDTO from(Vehicle vehicle){
        VehicleDTO dto = VehicleDTO.builder()
        .id(vehicle.getVehicleId())
        .licensePlate(vehicle.getLicensePlate())
        .model(vehicle.getModel())
        .brand(vehicle.getBrand())
        .registerDate(vehicle.getRegisterDate()) //TODO time

        .build();
        return dto;
    }
}
