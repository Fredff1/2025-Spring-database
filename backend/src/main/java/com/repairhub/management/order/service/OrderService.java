package com.repairhub.management.order.service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalEventPublisher;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.order.dto.CreateOrderRequest;
import com.repairhub.management.order.dto.OrderDTO;
import com.repairhub.management.order.entity.OrderAssignment;
import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.enums.OrderStatus;
import com.repairhub.management.order.event.OrderCreatedEvent;
import com.repairhub.management.order.repository.OrderAssignmentRepository;
import com.repairhub.management.order.repository.RepairOrderRepository;
import com.repairhub.management.repairman.entity.RepairmanProfile;
import com.repairhub.management.repairman.service.RepairmanProfileService;
import com.repairhub.management.vehicle.repository.VehicleRepository;

import lombok.Getter;

@Service
@Getter
public class OrderService {

    private final RepairOrderRepository repairOrderRepository;
    private final OrderAssignmentRepository orderAssignmentRepository;
    private final RepairmanProfileService repairmanProfileService;
    private final ApplicationEventPublisher eventPublisher;
    private final VehicleRepository vehicleRepository;

    public OrderService(
        RepairOrderRepository repairOrderRepository,
        OrderAssignmentRepository orderAssignmentRepository,
        RepairmanProfileService repairmanProfileService,
        VehicleRepository vehicleRepository,
        ApplicationEventPublisher eventPublisher) {
        this.repairOrderRepository = repairOrderRepository;
        this.orderAssignmentRepository = orderAssignmentRepository;
        this.repairmanProfileService = repairmanProfileService;
        this.vehicleRepository = vehicleRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public RepairOrder createOrder(User user, CreateOrderRequest request) {
        // 创建订单逻辑
        RepairOrder order = RepairOrder.builder()
                .userId(user.getUserId())
                .vehicleId(request.getVehicleId())
                .submitTime(java.time.LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .faultType(request.getFaultType())
                .status(OrderStatus.PENDING) // 假设初始状态为 PENDING
                .description(request.getDescription())
                .totalFee(BigDecimal.valueOf(0L))
                .build();
        repairOrderRepository.insert(order);
        OrderCreatedEvent event = new OrderCreatedEvent(this, order);
        eventPublisher.publishEvent(event);
        return order;
    }

    public Optional<RepairOrder> findById(Long orderId) {
        var order = repairOrderRepository.findById(orderId);
        if(order.isPresent()){
            List<OrderAssignment> assignments = orderAssignmentRepository.findByOrderId(order.get().getOrderId());
            if(assignments != null && !assignments.isEmpty()){
                List<Long> assignedRepairmanIds = new ArrayList<>();
                for (OrderAssignment assignment : assignments) {
                    if(assignment.getAccepted()){
                        assignedRepairmanIds.add(assignment.getRepairmanId());
                    }
                }
                order.get().setAssignedRepairmanIds(assignedRepairmanIds);
            }
        }
        
        return order;

    }

    public OrderDTO toDTO(RepairOrder order){
        return OrderDTO.from(order, vehicleRepository);
    }

    public OrderAssignment assignOrder(RepairOrder order){
        //TODO: 实现订单分配逻辑
        List<Long> filter = new ArrayList<>();
        List<OrderAssignment> assignments = orderAssignmentRepository.findByOrderId(order.getOrderId());
        if (assignments != null && !assignments.isEmpty()) {
            for (OrderAssignment assignment : assignments) {
                if(assignment.getAccepted()){
                    continue;
                }
                filter.add(assignment.getRepairmanId());
            }
        }

        List<RepairmanProfile> profiles = repairmanProfileService.findAllWithFilter(order.getFaultType(), filter);
        if(profiles.isEmpty()){
            profiles = repairmanProfileService.findAllWithFilter(null, List.of());
        }
        Random random = new Random();
        int index = random.nextInt(profiles.size());
        RepairmanProfile selectedProfile = profiles.get(index);
        OrderAssignment assignment = OrderAssignment.builder()
                .orderId(order.getOrderId())
                .repairmanId(selectedProfile.getUserId())
                .assignmentTime(LocalDateTime.now())
                .accepted(null)
                .actualWorkHour(null)
                .build();
        orderAssignmentRepository.insert(assignment);
        return assignment;
    }

    public List<RepairOrder> getOrders(User user){
        List<RepairOrder> orders = repairOrderRepository.findByUserId(user.getUserId());
        orders.sort(Comparator.comparing(RepairOrder::getSubmitTime).reversed());
        return orders;
    }
}
