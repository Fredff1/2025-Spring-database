package com.repairhub.management.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.repairhub.management.order.dto.CreateOrderRequest;
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
    private LocalDateTime updateTime;
    private OrderStatus status;
    private String description;
    private FaultType faultType;
    private BigDecimal totalFee;
    private Boolean isPaid;

    private List<Long> assignedRepairmanIds;

    public static RepairOrder from(CreateOrderRequest request, Long userId) {
        return RepairOrder.builder()
                .userId(userId)
                .vehicleId(request.getVehicleId())
                .submitTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .description(request.getDescription())
                .faultType(request.getFaultType())
                .totalFee(BigDecimal.ZERO) // Initial total fee is zero
                .isPaid(false)
                .build();
    }
}
