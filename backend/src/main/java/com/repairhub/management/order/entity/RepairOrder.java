package com.repairhub.management.order.entity;

import java.time.LocalDateTime;

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
    private String status;
    private String description;
}
