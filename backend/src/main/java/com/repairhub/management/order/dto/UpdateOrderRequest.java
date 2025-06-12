package com.repairhub.management.order.dto;

import java.math.BigDecimal;

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
public class UpdateOrderRequest {
    private OrderStatus orderStatus;
    private String description;
    private FaultType faultType;
    private BigDecimal totalFee;
    private Boolean isPaid;
}
