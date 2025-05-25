package com.repairhub.management.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderAssignment {
    private Long assignmentId;
    private Long orderId;
    private Long repairmanId;
    private LocalDateTime assignmentTime;
    private Boolean accepted;
    private BigDecimal actualWorkHour;
}
