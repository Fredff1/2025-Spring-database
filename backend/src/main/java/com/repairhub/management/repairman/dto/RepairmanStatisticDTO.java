package com.repairhub.management.repairman.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepairmanStatisticDTO {
    private Integer completedOrders;
    private BigDecimal repairHours;
    private BigDecimal totalIncome;
    private Double averageRating;
}
