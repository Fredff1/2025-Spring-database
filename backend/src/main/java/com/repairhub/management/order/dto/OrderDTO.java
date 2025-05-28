package com.repairhub.management.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.enums.OrderStatus;
import com.repairhub.management.repair.enums.RepairType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private String orderNo;
    private Long vehicleId;
    private String vehiclePlate;
    private RepairType repairType;
    private String problem;
    private OrderStatus status;
    private BigDecimal amount;
    private Boolean isPaid;
    private Boolean isReviewed;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public static OrderDTO from(RepairOrder order) {
        return OrderDTO.builder()
                .id(order.getOrderId())
                .orderNo("ORD" + order.getOrderId()) // Example order number generation
                .vehicleId(order.getVehicleId())
                .vehiclePlate("Plate" + order.getVehicleId()) // Example vehicle plate generation
                .repairType(order.getFaultType() != null ? RepairType.valueOf(order.getFaultType().name()) : null)
                .problem(order.getDescription())
                .status(order.getStatus())
                .amount(order.getTotalFee())
                .isPaid(false) // Assuming initial state is unpaid
                .isReviewed(false) // Assuming initial state is not reviewed
                .createTime(order.getSubmitTime())
                .updateTime(LocalDateTime.now()) // Assuming update time is now
                .build();
    }

    public static OrderDTO toDefault(){
        OrderDTO dto = OrderDTO.builder()
        .id(1L)
        .orderNo("1")
        .vehicleId(1L)
        .status(OrderStatus.PENDING)
        .problem("no")
        .repairType(RepairType.ACCIDENT)
        .vehiclePlate("default")
        .amount(BigDecimal.valueOf(10))
        .isPaid(false)
        .isReviewed(false)
        .createTime(LocalDateTime.now())
        .updateTime(LocalDateTime.now())
        .build();
        return dto;
    }
}
