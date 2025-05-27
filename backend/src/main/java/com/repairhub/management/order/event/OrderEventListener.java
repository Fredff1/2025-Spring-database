package com.repairhub.management.order.event;


import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.repairhub.management.order.entity.OrderAssignment;
import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.service.OrderService;
import com.repairhub.management.repair.service.RepairFeeService;

@Component
public class OrderEventListener {

    private final OrderService orderService;
    private final RepairFeeService repairFeeService;

    public OrderEventListener(
        OrderService orderService,
        RepairFeeService repairFeeService) {
        this.repairFeeService = repairFeeService;
        this.orderService = orderService;
    }
    
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onOrderCreated(OrderCreatedEvent event) {
        // 处理订单创建事件
        orderService.assignOrder(event.getRepairOrder());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onAssignmentCoped(AssignmentCopedEvent event) {
        OrderAssignment assignment = event.getOrderAssignment();
        if(!assignment.getAccepted()){
            // 如果维修工拒绝了订单分配，则将订单状态改为 PENDING
            orderService.findById(assignment.getOrderId()).ifPresent(order -> {
                orderService.assignOrder(order);
            });
        } else {
            // TODO 进一步处理
        }
        
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onOrderCompleted(OrderCompletedEvent event) {
        // 处理订单完成事件
        RepairOrder order = event.getRepairOrder();
        BigDecimal fee = repairFeeService.calculateFeeByOrder(order);
        order.setTotalFee(fee);
        orderService.getRepairOrderRepository().update(order);
    }
}
