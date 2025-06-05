package com.repairhub.management.statistic.dto;

import com.repairhub.management.repair.enums.FaultType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleModelFaultTypeCountDTO {
    private String model;
    private FaultType faultType;  
    private Long count;    
}
