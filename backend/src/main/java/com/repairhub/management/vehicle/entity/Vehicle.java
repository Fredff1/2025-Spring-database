package com.repairhub.management.vehicle.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vehicle {
    private Long vehicleId;
    private Long ownerId;
    private String brand;
    private String model;
    private String licensePlate;
    private LocalDate registerDate;
}
