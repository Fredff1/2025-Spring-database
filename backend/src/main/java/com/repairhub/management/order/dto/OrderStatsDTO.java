package com.repairhub.management.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatsDTO {
    private Integer vehicleCount;
    private Integer orderCount;
    private Integer historyCount;
    
}
