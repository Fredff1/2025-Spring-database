package com.repairhub.management.auth.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepairmanProfile {
    private Long userId;
    private String specialty;
    private BigDecimal hourlyMoneyRate;
}
