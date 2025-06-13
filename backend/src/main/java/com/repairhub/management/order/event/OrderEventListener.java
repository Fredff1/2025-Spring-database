package com.repairhub.management.order.event;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.order.entity.OrderAssignment;
import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.enums.AssignmentStatus;
import com.repairhub.management.order.enums.OrderStatus;
import com.repairhub.management.order.repository.OrderAssignmentRepository;
import com.repairhub.management.order.service.OrderService;
import com.repairhub.management.repair.dto.CreateLaborFeeLogDTO;
import com.repairhub.management.repair.service.RepairFeeService;
import com.repairhub.management.repairman.repository.RepairmanProfileRepository;

@Component
public class OrderEventListener {

    private final OrderService orderService;
    private final RepairFeeService repairFeeService;
    private final RepairmanProfileRepository profileRepository;
    private final OrderAssignmentRepository orderAssignmentRepository;

    public OrderEventListener(
        OrderService orderService,
        RepairFeeService repairFeeService,
        RepairmanProfileRepository profileRepository,
        OrderAssignmentRepository orderAssignmentRepository) {
        this.repairFeeService = repairFeeService;
        this.orderService = orderService;
        this.profileRepository = profileRepository;
        this.orderAssignmentRepository = orderAssignmentRepository;
    }
    
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onOrderCreated(OrderCreatedEvent event) {
        orderService.assignOrder(event.getRepairOrder());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onAssignmentCoped(AssignmentCopedEvent event) {
        OrderAssignment assignment = event.getOrderAssignment();
        if(!assignment.getStatus().isAccepted()){
            orderService.findById(assignment.getOrderId()).ifPresent(order -> {
                var asgRepo = orderService.getOrderAssignmentRepository();
                Long orderId = order.getOrderId();
                if(asgRepo.existByOrderIdAndStatus(orderId,AssignmentStatus.ACCEPTED) 
                  || asgRepo.existByOrderIdAndStatus(orderId,AssignmentStatus.PENDING)){

                }else{
                    orderService.assignOrder(order);
                }
                
            });
        } else {
            orderService.findById(assignment.getOrderId()).ifPresent(order -> {
                order.setStatus(OrderStatus.PROCESSING);
                orderService.getRepairOrderRepository().update(order);
            });
        }
        
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderCoped(OrderDealtEvent event) {
        RepairOrder order = event.getRepairOrder();
        repairFeeService.createLaborFeeLog(event.getCreateLaborFeeLogDTO());
        BigDecimal fee = repairFeeService.calculateFeeByOrder(order);
        order.setTotalFee(fee);
        orderService.getRepairOrderRepository().update(order);
        orderService.finishOrder(order);
    }
}
