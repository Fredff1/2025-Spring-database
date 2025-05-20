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
public class OverviewDTO {
    private Integer todayOrders;
    private Integer orderTrend;
    private Integer pendingOrders;
    private BigDecimal monthlyIncome;
    private Integer incomeTrend;
    private BigDecimal rating;
}
