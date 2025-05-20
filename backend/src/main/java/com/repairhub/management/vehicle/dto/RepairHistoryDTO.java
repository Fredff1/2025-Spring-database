package com.repairhub.management.vehicle.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.repairhub.management.repair.enums.RepairType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepairHistoryDTO {
    private Long id;
    private Long orderId;
    private RepairType repairType;
    private String problem;
    private String solution;
    private List<String> partsUsed;
    private Integer laborHours;
    private BigDecimal amount;
    private LocalDateTime createTime;
}
