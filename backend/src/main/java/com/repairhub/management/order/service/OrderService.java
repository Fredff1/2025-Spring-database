package com.repairhub.management.order.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalEventPublisher;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.order.dto.CreateOrderRequest;
import com.repairhub.management.order.entity.OrderAssignment;
import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.event.OrderCreatedEvent;
import com.repairhub.management.order.repository.OrderAssignmentRepository;
import com.repairhub.management.order.repository.RepairOrderRepository;

@Service
public class OrderService {

    private final RepairOrderRepository repairOrderRepository;
    private final OrderAssignmentRepository orderAssignmentRepository;
    private final TransactionalEventPublisher eventPublisher;

    public OrderService(
        RepairOrderRepository repairOrderRepository,
        OrderAssignmentRepository orderAssignmentRepository,
        TransactionalEventPublisher eventPublisher) {
        this.repairOrderRepository = repairOrderRepository;
        this.orderAssignmentRepository = orderAssignmentRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public RepairOrder createOrder(User user, CreateOrderRequest request) {
        // 创建订单逻辑
        RepairOrder order = RepairOrder.builder()
                .userId(user.getUserId())
                .vehicleId(request.getVehicleId())
                .submitTime(java.time.LocalDateTime.now())
                .status("PENDING") // 假设初始状态为 PENDING
                .description(request.getProblem())
                .build();
        repairOrderRepository.insert(order);
        OrderCreatedEvent event = new OrderCreatedEvent(this, order);
        eventPublisher.publishEvent(event);
        return order;
    }

    public OrderAssignment assignOrder(RepairOrder order){
        //TODO: 实现订单分配逻辑
        return null;
    }
}
