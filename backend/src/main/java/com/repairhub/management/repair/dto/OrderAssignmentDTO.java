package com.repairhub.management.repair.dto;

import java.time.LocalDateTime;

import com.repairhub.management.order.entity.OrderAssignment;
import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.enums.AssignmentStatus;
import com.repairhub.management.repair.enums.FaultType;
import com.repairhub.management.utils.OrderUtil;
import com.repairhub.management.vehicle.entity.Vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderAssignmentDTO {
    private Long assignmentId;
    private Long orderId;
    private String orderNumber;
    private String plateNumber;
    private FaultType repairType;
    private String problem;
    private LocalDateTime assignmentTime;
    private AssignmentStatus status;

    public static OrderAssignmentDTO from(
        OrderAssignment assignment,
        RepairOrder order,
        Vehicle vehicle
    ){
        OrderAssignmentDTO dto = OrderAssignmentDTO
        .builder()
        .assignmentId(assignment.getAssignmentId())
        .orderId(assignment.getOrderId())
        .orderNumber(OrderUtil.getOrderNumber(assignment.getOrderId()))
        .plateNumber(vehicle.getLicensePlate())
        .repairType(order.getFaultType())
        .problem(order.getDescription())
        .assignmentTime(assignment.getAssignmentTime())
        .status(assignment.getStatus())
        .build();
        return dto;
    }
}
