package com.repairhub.management.admin.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticsDTO {
    private Integer completedOrders;
    private Integer repairHours;
    private BigDecimal income;
    private BigDecimal rating;
}
