package com.repairhub.management.repairman.entity;

import java.math.BigDecimal;
import java.util.List;

import com.repairhub.management.order.entity.OrderAssignment;
import com.repairhub.management.repair.enums.FaultType;

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
    private FaultType specialty;
    private BigDecimal hourlyMoneyRate;

    private List<OrderAssignment> orderAssignments;
}
