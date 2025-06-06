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
public class AdminOverviewDTO {
    private Integer todayOrders;
    private Integer unfinishedOrders;
    private BigDecimal monthlyCost;
    private Integer activeRepairmen;
}
