package com.repairhub.management.repair.dto;

import com.repairhub.management.order.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateRepairRecordDTO {
    private Long orderId;
    private String faultDescription;
    private String repairResult;
    private OrderStatus orderStatus;
}
