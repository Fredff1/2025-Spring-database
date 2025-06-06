package com.repairhub.management.statistic.dto;

import com.repairhub.management.order.enums.AssignmentStatus;
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
public class OrderProcessStatDTO {
   private Long pendingOrders;
   private Long processingOrders;
   private Long completedOrders;
   private Long totalOrders;
   private FaultType faultType;

}
