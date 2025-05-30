package com.repairhub.management.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.repairhub.management.order.enums.AssignmentStatus;

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
    private AssignmentStatus status;
    private BigDecimal actualWorkHour;
}
