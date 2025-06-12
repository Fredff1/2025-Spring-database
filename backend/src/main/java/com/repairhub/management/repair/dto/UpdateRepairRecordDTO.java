package com.repairhub.management.repair.dto;

import java.math.BigDecimal;

import com.repairhub.management.order.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRepairRecordDTO {
    private String faultDescription;
    private String repairResult;
    private OrderStatus orderStatus;
    private BigDecimal actualWorkingHours;
}
