package com.repairhub.management.repairman.dto;

import java.math.BigDecimal;
import java.util.List;

import com.repairhub.management.repair.dto.LaborFeeLogDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepairmanIncomeDTO {
    private List<LaborFeeLogDTO> list;
    private Integer total;
    private BigDecimal totalIncome;
    private BigDecimal totalHours;
    private BigDecimal averageHourlyRate;
}
