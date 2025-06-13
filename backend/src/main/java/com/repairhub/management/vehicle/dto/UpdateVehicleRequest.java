package com.repairhub.management.vehicle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateVehicleRequest {
    private String licensePlate;
    private String brand;
    private String model;
}
