package com.repairhub.management.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.repairhub.management.order.enums.OrderStatus;
import com.repairhub.management.repair.enums.FaultType;
import com.repairhub.management.repair.enums.RepairType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {
    private Long vehicleId;
    private String description;
    private FaultType faultType;
}
