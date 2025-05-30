package com.repairhub.management.order.event;


import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.order.entity.OrderAssignment;
import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.enums.OrderStatus;
import com.repairhub.management.order.service.OrderService;
import com.repairhub.management.repair.dto.CreateLaborFeeLogDTO;
import com.repairhub.management.repair.service.RepairFeeService;
import com.repairhub.management.repairman.repository.RepairmanProfileRepository;

@Component
public class OrderEventListener {

    private final OrderService orderService;
    private final RepairFeeService repairFeeService;
    private final RepairmanProfileRepository profileRepository;

    public OrderEventListener(
        OrderService orderService,
        RepairFeeService repairFeeService,
        RepairmanProfileRepository profileRepository) {
        this.repairFeeService = repairFeeService;
        this.orderService = orderService;
        this.profileRepository = profileRepository;
    }
    
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onOrderCreated(OrderCreatedEvent event) {
        // 处理订单创建事件
        orderService.assignOrder(event.getRepairOrder());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onAssignmentCoped(AssignmentCopedEvent event) {
        OrderAssignment assignment = event.getOrderAssignment();
        if(!assignment.getStatus().isAccepted()){
            // 如果维修工拒绝了订单分配，则将订单状态改为 PENDING
            // orderService.findById(assignment.getOrderId()).ifPresent(order -> {
            //     order.setStatus(OrderStatus.PENDING);
            //     orderService.getRepairOrderRepository().update(order);
            //     orderService.assignOrder(order);
            // });
        } else {
            orderService.findById(assignment.getOrderId()).ifPresent(order -> {
                // 如果维修工接受了订单分配，则将订单状态改为 PROCESSING
                order.setStatus(OrderStatus.PROCESSING);
                orderService.getRepairOrderRepository().update(order);
            });
        }
        
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderCoped(OrderDealtEvent event) {
        // 处理订单完成事件
        RepairOrder order = event.getRepairOrder();
        repairFeeService.createLaborFeeLog(event.getCreateLaborFeeLogDTO());
        BigDecimal fee = repairFeeService.calculateFeeByOrder(order);
        order.setTotalFee(fee);
        orderService.getRepairOrderRepository().update(order);
    }
}
