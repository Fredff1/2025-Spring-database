package com.repairhub.management.repair.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.repairhub.management.order.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepairRecord {
    private Long recordId;
    private Long orderId;
    private Long repairmanId;
    private String faultDescription;
    private String repairResult;
    private LocalDateTime completionTime;
    private BigDecimal actualWorkHour;
    private OrderStatus orderStatus;
}
