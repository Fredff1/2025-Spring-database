package com.repairhub.management.order.event;


import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.repairhub.management.order.entity.OrderAssignment;
import com.repairhub.management.order.service.OrderService;

@Component
public class OrderEventListener {

    private final OrderService orderService;

    public OrderEventListener(OrderService orderService) {
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
}
