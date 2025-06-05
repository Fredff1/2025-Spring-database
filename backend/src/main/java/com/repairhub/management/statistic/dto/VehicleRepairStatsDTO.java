package com.repairhub.management.statistic.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleRepairStatsDTO {
    private String model;   
    private Long repairCount;   
    private BigDecimal averageCost;   
}
