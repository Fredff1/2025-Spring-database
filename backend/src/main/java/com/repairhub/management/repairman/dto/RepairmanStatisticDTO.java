package com.repairhub.management.repairman.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepairmanStatisticDTO {
    private Integer completedOrders;
    private Integer repairHours;
    private Integer income;
    private Integer rating;
}
