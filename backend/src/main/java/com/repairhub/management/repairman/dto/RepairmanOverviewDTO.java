package com.repairhub.management.repairman.dto;

import java.math.BigDecimal;

import org.springframework.data.relational.core.sql.In;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepairmanOverviewDTO {
    private Integer totalAssignments;
    private Integer processingOrders;
    private BigDecimal monthlyIncome;
    private Double rating;
}
