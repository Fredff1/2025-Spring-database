package com.repairhub.management.repair.entity;

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
public class LaborFeeLog {
    private Long LaborFeeLogId;
    private Long repairmanId;
    private Month month;
    private BigDecimal totalHours;
    private BigDecimal totalIncome;
    private LocalDateTime settleTime;

    
}
