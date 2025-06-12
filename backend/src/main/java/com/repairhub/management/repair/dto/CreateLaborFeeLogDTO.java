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
    private Long orderId;
    private Long recordId;
    private Long repairmanId;
    private BigDecimal totalHours;
    private BigDecimal totalIncome;

    public static CreateLaborFeeLogDTO from(
        Long orderId,
        Long repairmanId,
        Long recordId,
        BigDecimal totalHours,
        BigDecimal hourlyMoneyRate
    ){
        CreateLaborFeeLogDTO dto = CreateLaborFeeLogDTO.builder()
        .orderId(orderId)
        .repairmanId(repairmanId)
        .totalHours(totalHours)
        .totalIncome(hourlyMoneyRate.multiply(totalHours))
        .recordId(recordId)
        .build();
        return dto;
    }
}
