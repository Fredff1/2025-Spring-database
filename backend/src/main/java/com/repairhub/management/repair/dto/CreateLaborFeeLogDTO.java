package com.repairhub.management.repair.dto;

import java.math.BigDecimal;
import java.time.Month;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateLaborFeeLogDTO {
    private Month month;
    private Long repairmanId;
    private BigDecimal totalHours;
    private BigDecimal totalIncome;
}
