package com.repairhub.management.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.auth.repository.UserRepository;
import com.repairhub.management.order.entity.OrderAssignment;
import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.repository.OrderAssignmentRepository;
import com.repairhub.management.order.repository.RepairOrderRepository;
import com.repairhub.management.repair.dto.OrderAssignmentDTO;
import com.repairhub.management.repairman.service.RepairmanProfileService;
import com.repairhub.management.vehicle.entity.Vehicle;
import com.repairhub.management.vehicle.repository.VehicleRepository;

import lombok.Getter;

@Service
@Getter
public class OrderAssignmentService {
    private final UserRepository userRepository;
    private final OrderAssignmentRepository assignmentRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final VehicleRepository vehicleRepository;
    private final RepairmanProfileService repairmanProfileService;

    public OrderAssignmentService(
        UserRepository userRepository,
        OrderAssignmentRepository assignmentRepository,
        RepairOrderRepository repairOrderRepository,
        VehicleRepository vehicleRepository,
        RepairmanProfileService repairmanProfileService
    ){  
        this.userRepository = userRepository;
        this.assignmentRepository = assignmentRepository;
        this.repairOrderRepository = repairOrderRepository;
        this.vehicleRepository = vehicleRepository;
        this.repairmanProfileService = repairmanProfileService;
    }

    public List<OrderAssignment> getRepairmanAssignments(Long repairmanId){
        List<OrderAssignment> assignments = assignmentRepository.findByRepairmanId(repairmanId);
        return assignments;
    }

    public List<OrderAssignment> getAllAssignments(){
        List<OrderAssignment> assignments = assignmentRepository.findAll();
        return assignments;
    }

    public OrderAssignmentDTO convertAssignmentToDTO(OrderAssignment assignment){
        RepairOrder order = repairOrderRepository.findById(assignment.getOrderId()).get();
        Vehicle vehicle = vehicleRepository.findById(order.getVehicleId()).get();
        User user = userRepository.findById(assignment.getRepairmanId()).get();
        OrderAssignmentDTO dto = OrderAssignmentDTO.from(assignment, order, vehicle,user);
        return dto;
    }

    public void acceptAssignment(Long assignmentId){
        OrderAssignment assignment = assignmentRepository.findById(assignmentId).get();
        repairmanProfileService.copeWithOrderAssignment(assignment, true);
    }

    public void rejectAssignment(Long assignmentId){
        OrderAssignment assignment = assignmentRepository.findById(assignmentId).get();
        repairmanProfileService.copeWithOrderAssignment(assignment, false);
    }
}
