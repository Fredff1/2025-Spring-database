package com.repairhub.management.vehicle.dto;

import java.time.LocalDateTime;

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
    private String plateNumber;
    private String brand;
    private String model;
    private Integer year;
    private String vin;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
