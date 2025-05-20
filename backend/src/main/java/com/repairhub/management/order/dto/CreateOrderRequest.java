package com.repairhub.management.order.dto;

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
    private RepairType repairType;
    private String problem;
}
