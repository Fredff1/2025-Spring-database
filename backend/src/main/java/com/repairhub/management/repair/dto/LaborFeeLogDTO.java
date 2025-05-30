package com.repairhub.management.repair.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LaborFeeLogDTO {
    private Long LaborFeeLogId;
    private Long orderId;
    private Long repairmanId;
    private Month month;
    private BigDecimal hourlyMoneyRate;
    private BigDecimal totalHours;
    private BigDecimal totalIncome;
    private LocalDateTime settleTime;

}
