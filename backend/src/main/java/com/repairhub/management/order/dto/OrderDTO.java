package com.repairhub.management.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.enums.OrderStatus;
import com.repairhub.management.repair.enums.FaultType;
import com.repairhub.management.repair.enums.RepairType;
import com.repairhub.management.utils.OrderUtil;
import com.repairhub.management.vehicle.entity.Vehicle;
import com.repairhub.management.vehicle.repository.VehicleRepository;

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
    private FaultType repairType;
    private String problem;
    private OrderStatus status;
    private BigDecimal amount;
    private Boolean isPaid;
    private Boolean isReviewed;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public static OrderDTO from(RepairOrder order,VehicleRepository vehicleRepository) {
        Vehicle vehicle = vehicleRepository.findById(order.getVehicleId()).get();
        var dto =OrderDTO.builder()
            .id(order.getOrderId())
            .orderNo(OrderUtil.getOrderNumber(order.getOrderId())) // Example order number generation
            .vehicleId(order.getVehicleId())
            .vehiclePlate(vehicle.getLicensePlate()) // Example vehicle plate generation
            .repairType(order.getFaultType())
            .problem(order.getDescription())
            .status(order.getStatus())
            .amount(order.getTotalFee())
            // .isPaid(false) // Assuming initial state is unpaid
            // .isReviewed(false) // Assuming initial state is not reviewed
            .createTime(order.getSubmitTime())
            .updateTime(order.getUpdateTime()) // Assuming update time is now 
            // TODO update time
            .build();
        return dto;
    }

    public static OrderDTO toDefault(){
        OrderDTO dto = OrderDTO.builder()
        .id(1L)
        .orderNo("1")
        .vehicleId(1L)
        .status(OrderStatus.PENDING)
        .problem("no")
        .repairType(FaultType.MAINTENANCE)
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
