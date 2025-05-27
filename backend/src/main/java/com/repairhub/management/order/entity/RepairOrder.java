package com.repairhub.management.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.repairhub.management.order.enums.OrderStatus;
import com.repairhub.management.repair.enums.FaultType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepairOrder {
    private Long orderId;
    private Long userId;
    private Long vehicleId;
    private LocalDateTime submitTime;
    private OrderStatus status;
    private String description;
    private FaultType faultType;
    private BigDecimal totalFee;
}
