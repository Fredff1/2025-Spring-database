package com.repairhub.management.init;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.repairhub.management.order.entity.OrderAssignment;
import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.enums.AssignmentStatus;
import com.repairhub.management.order.enums.OrderStatus;
import com.repairhub.management.order.repository.OrderAssignmentRepository;
import com.repairhub.management.order.repository.RepairOrderRepository;
import com.repairhub.management.order.service.OrderService;
import com.repairhub.management.repair.entity.LaborFeeLog;
import com.repairhub.management.repair.entity.MaterialUsage;
import com.repairhub.management.repair.entity.RepairFeedback;
import com.repairhub.management.repair.entity.RepairRecord;
import com.repairhub.management.repair.enums.FeedbackType;
import com.repairhub.management.repair.repository.LaborFeeLogRepository;
import com.repairhub.management.repair.repository.MaterialUsageRepository;
import com.repairhub.management.repair.repository.RepairFeedbackRepository;
import com.repairhub.management.repair.repository.RepairRecordRepository;
import com.repairhub.management.repair.service.RepairFeeService;

@Component
@Order(10)
public class OrderInfoInitializer implements ApplicationRunner{

    private final RepairFeedbackRepository feedbackRepository;
    private final RepairRecordRepository recordRepository;
    private final MaterialUsageRepository materialUsageRepository;
    private final LaborFeeLogRepository laborFeeLogRepository;
    private final RepairOrderRepository orderRepository;
    private final RepairFeeService repairFeeService;
    private final OrderService orderService;
    private final OrderAssignmentRepository orderAssignmentRepository;

    public OrderInfoInitializer(
        RepairFeedbackRepository feedbackRepository,
        RepairRecordRepository recordRepository,
        MaterialUsageRepository materialUsageRepository,
        LaborFeeLogRepository laborFeeLogRepository,
        RepairOrderRepository orderRepository,
        RepairFeeService repairFeeService,
        OrderService orderService,
        OrderAssignmentRepository orderAssignmentRepository
    ){
        this.feedbackRepository = feedbackRepository;
        this.recordRepository = recordRepository;
        this.materialUsageRepository = materialUsageRepository;
        this.laborFeeLogRepository = laborFeeLogRepository;
        this.orderRepository = orderRepository;
        this.repairFeeService = repairFeeService;
        this.orderService = orderService;
        this.orderAssignmentRepository = orderAssignmentRepository;
    }
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //initData();
        updateOrders();
    }

    private void updateOrders(){
        List<RepairOrder> orders = orderRepository.findAll();
        for(var order : orders){
            List<OrderAssignment> assignments = orderAssignmentRepository.findByOrderId(order.getOrderId());
            for(var assignment: assignments){
                if(assignment.getStatus().equals(AssignmentStatus.ACCEPTED)){
                    if(order.getStatus().equals(OrderStatus.PENDING)){
                        order.setStatus(OrderStatus.PROCESSING);
                    }
                    
                }
            }
            BigDecimal fee = repairFeeService.calculateFeeByOrder(order);
            order.setTotalFee(fee);
            orderRepository.update(order);
            if(order.getStatus().equals(OrderStatus.COMPLETED)){
                orderService.finishOrder(order);
            }
            
        }
    }

    private void initData(){
        RepairFeedback feedback = RepairFeedback.builder()
        .orderId(1L)
        .userId(1L)
        .rating(4)
        .feedbackType(FeedbackType.RATING)
        .description("到目前为止，服务还不错")
        .feedbackTime(LocalDateTime.now())
        .build();
        feedbackRepository.insert(feedback);

        RepairFeedback feedback_1 = RepairFeedback.builder()
        .orderId(1L)
        .userId(1L)
        .rating(2)
        .feedbackType(FeedbackType.URGENT)
        .description("后续维修太慢了！")
        .feedbackTime(LocalDateTime.now())
        .build();
        feedbackRepository.insert(feedback_1);

        RepairFeedback feedback_2 = RepairFeedback.builder()
        .orderId(1L)
        .userId(1L)
        .rating(4)
        .feedbackType(FeedbackType.GENERAL)
        .description("维修总体表现还可以")
        .feedbackTime(LocalDateTime.now())
        .build();
        feedbackRepository.insert(feedback_2);

        RepairRecord record = RepairRecord.builder()
        .orderId(1L)
        .repairmanId(2L)
        .faultDescription("用太久了需要修复")
        .repairResult("初期修理一切顺利")
        .completionTime(LocalDateTime.now())
        .orderStatus(OrderStatus.PROCESSING)
        .actualWorkHour(BigDecimal.valueOf(2L))
        .build();
        recordRepository.insert(record);

        LaborFeeLog laborFeeLog = LaborFeeLog.builder()
        .orderId(1L)
        .repairmanId(2L)
        .month(LocalDateTime.now().getMonth())
        .totalHours(BigDecimal.valueOf(2L))
        .totalIncome(BigDecimal.valueOf(200L))
        .settleTime(LocalDateTime.now())
        .build();
        laborFeeLogRepository.insert(laborFeeLog);

        LaborFeeLog laborFeeLog_1 = LaborFeeLog.builder()
        .orderId(1L)
        .repairmanId(2L)
        .month(LocalDateTime.now().getMonth())
        .totalHours(BigDecimal.valueOf(2L))
        .totalIncome(BigDecimal.valueOf(200L))
        .settleTime(LocalDateTime.now())
        .build();
        laborFeeLogRepository.insert(laborFeeLog_1);

        RepairRecord record_1 = RepairRecord.builder()
        .orderId(1L)
        .repairmanId(2L)
        .faultDescription("用太久了需要修复")
        .repairResult("中期修理完成")
        .orderStatus(OrderStatus.PROCESSING)
        .completionTime(LocalDateTime.now())
        .actualWorkHour(BigDecimal.valueOf(2L))
        .build();
        recordRepository.insert(record_1);

        MaterialUsage materialUsage = MaterialUsage.builder()
        .orderId(1L)
        .materialName("喷漆(瓶装)")
        .quantity(1)
        .unitPrice(BigDecimal.valueOf(100L))
        .createTime(LocalDateTime.now())
        .build();
        materialUsageRepository.insert(materialUsage);

        RepairOrder order = orderRepository.findById(1L).get();
        BigDecimal fee = repairFeeService.calculateFeeByOrder(order);
        order.setTotalFee(fee);
        orderRepository.update(order);

    }

}
