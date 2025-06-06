package com.repairhub.management.order.service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalEventPublisher;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.auth.repository.UserRepository;
import com.repairhub.management.common.dto.PageResponse;
import com.repairhub.management.order.dto.CreateOrderRequest;
import com.repairhub.management.order.dto.OrderDTO;
import com.repairhub.management.order.entity.OrderAssignment;
import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.enums.AssignmentStatus;
import com.repairhub.management.order.enums.OrderStatus;
import com.repairhub.management.order.event.OrderCreatedEvent;
import com.repairhub.management.order.repository.OrderAssignmentRepository;
import com.repairhub.management.order.repository.RepairOrderRepository;
import com.repairhub.management.repairman.entity.RepairmanProfile;
import com.repairhub.management.repairman.repository.RepairmanBaseInfoRepository;
import com.repairhub.management.repairman.service.RepairmanProfileService;
import com.repairhub.management.utils.OrderUtil;
import com.repairhub.management.vehicle.entity.Vehicle;
import com.repairhub.management.vehicle.repository.VehicleRepository;

import lombok.Getter;

@Service
@Getter
public class OrderService {

    private final UserRepository userRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final OrderAssignmentRepository orderAssignmentRepository;
    private final RepairmanProfileService repairmanProfileService;
    private final ApplicationEventPublisher eventPublisher;
    private final VehicleRepository vehicleRepository;
    private final RepairmanBaseInfoRepository repairmanBaseInfoRepository; 

    private String assignType = "ALL";

    public OrderService(
        UserRepository userRepository,
        RepairOrderRepository repairOrderRepository,
        OrderAssignmentRepository orderAssignmentRepository,
        RepairmanProfileService repairmanProfileService,
        VehicleRepository vehicleRepository,
        ApplicationEventPublisher eventPublisher,
        RepairmanBaseInfoRepository repairmanBaseInfoRepository
    ) {
        this.userRepository = userRepository;
        this.repairOrderRepository = repairOrderRepository;
        this.orderAssignmentRepository = orderAssignmentRepository;
        this.repairmanProfileService = repairmanProfileService;
        this.vehicleRepository = vehicleRepository;
        this.eventPublisher = eventPublisher;
        this.repairmanBaseInfoRepository = repairmanBaseInfoRepository;
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
                .isPaid(false)
                .build();
        repairOrderRepository.insert(order);
        OrderCreatedEvent event = new OrderCreatedEvent(this, order);
        eventPublisher.publishEvent(event);
        return order;
    }

    @Transactional
    public List<RepairOrder> createBatchOrder(User user,List<CreateOrderRequest> requests){
        List<ApplicationEvent> events = new ArrayList<>();
        List<RepairOrder> orders = new ArrayList<>();
        for(var request : requests){
            RepairOrder order = RepairOrder.builder()
                .userId(user.getUserId())
                .vehicleId(request.getVehicleId())
                .submitTime(java.time.LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .faultType(request.getFaultType())
                .status(OrderStatus.PENDING) // 假设初始状态为 PENDING
                .description(request.getDescription())
                .totalFee(BigDecimal.valueOf(0L))
                .isPaid(false)
                .build();
            repairOrderRepository.insert(order);
            OrderCreatedEvent event = new OrderCreatedEvent(this, order);
            events.add(event);
            orders.add(order);
        }
        for (ApplicationEvent event : events) {
            eventPublisher.publishEvent(event);
        }
        return orders;
    }

    @Transactional
    public void cancelOrder(Long orderId){
        var order = repairOrderRepository.findById(orderId).get();
        order.setStatus(OrderStatus.CANCELLED);
        List<OrderAssignment> assignments = orderAssignmentRepository.findByOrderId(order.getOrderId());
        for(var assignment: assignments){
            assignment.setStatus(AssignmentStatus.CANCELED);
            orderAssignmentRepository.update(assignment);
        }
        repairOrderRepository.update(order);
    }

    public Optional<RepairOrder> findById(Long orderId) {
        var order = repairOrderRepository.findById(orderId);
        if(order.isPresent()){
            List<OrderAssignment> assignments = orderAssignmentRepository.findByOrderId(order.get().getOrderId());
            if(assignments != null && !assignments.isEmpty()){
                List<Long> assignedRepairmanIds = new ArrayList<>();
                for (OrderAssignment assignment : assignments) {
                    if(assignment.getStatus().isAccepted()){
                        assignedRepairmanIds.add(assignment.getRepairmanId());
                    }
                }
                order.get().setAssignedRepairmanIds(assignedRepairmanIds);
            }
        }
        
        return order;
    }

    public void updateRepairOrder(RepairOrder order){
        repairOrderRepository.update(order);
    }

    public OrderDTO toDTO(RepairOrder order){
        User user = userRepository.findById(order.getUserId()).orElse(null);
        Vehicle vehicle = vehicleRepository.findById(order.getVehicleId()).get();
        var dto =OrderDTO.builder()
            .id(order.getOrderId())
            .orderNo(OrderUtil.getOrderNumber(order.getOrderId())) 
            .vehicleId(order.getVehicleId())
            .vehiclePlate(vehicle.getLicensePlate()) 
            .repairType(order.getFaultType())
            .problem(order.getDescription())
            .status(order.getStatus())
            .amount(order.getTotalFee())
            .isPaid(order.getIsPaid())
            .customerName(user.getUsername())
            .createTime(order.getSubmitTime())
            .updateTime(order.getUpdateTime()) 
            .repairmanBaseInfos(repairmanBaseInfoRepository.findAllByOrderId(order.getOrderId()))
            .build();
        return dto;
    }

    @Transactional
    public void assignOrder(RepairOrder order){
        List<Long> filter = new ArrayList<>();
        List<OrderAssignment> assignments = orderAssignmentRepository.findByOrderId(order.getOrderId());
        if (assignments != null && !assignments.isEmpty()) {
            for (OrderAssignment assignment : assignments) {
                filter.add(assignment.getRepairmanId());
            }
        }

        List<RepairmanProfile> profiles = repairmanProfileService.findAllWithFilter(order.getFaultType(), filter);
        if(profiles.isEmpty()){
            profiles = repairmanProfileService.findAllWithFilter(null, filter);
            if(profiles.isEmpty()){
                // 兜底维修人
                User repairman = userRepository.findByUsername("duty_repair").get();
                OrderAssignment assignment = OrderAssignment.builder()
                    .orderId(order.getOrderId())
                    .repairmanId(repairman.getUserId())
                    .assignmentTime(LocalDateTime.now())
                    .status(AssignmentStatus.ACCEPTED)
                    .build();
                orderAssignmentRepository.insert(assignment);
                order.setStatus(OrderStatus.PROCESSING);
                repairOrderRepository.update(order);
                return;
            }
        }
        if(assignType.equals("ALL")){
            for(RepairmanProfile selectedProfile : profiles){
                OrderAssignment assignment = OrderAssignment.builder()
                    .orderId(order.getOrderId())
                    .repairmanId(selectedProfile.getUserId())
                    .assignmentTime(LocalDateTime.now())
                    .status(AssignmentStatus.PENDING)
                    .build();
                orderAssignmentRepository.insert(assignment);
            }
        }else{
            Random random = new Random();
            int index = random.nextInt(profiles.size());
            RepairmanProfile selectedProfile = profiles.get(index);
            OrderAssignment assignment = OrderAssignment.builder()
                .orderId(order.getOrderId())
                .repairmanId(selectedProfile.getUserId())
                .assignmentTime(LocalDateTime.now())
                .status(AssignmentStatus.PENDING)
                .build();
            orderAssignmentRepository.insert(assignment);
            
        }

        
    }

    @Transactional
    public void finishOrder(RepairOrder order){
        Long orderId = order.getOrderId();
        List<OrderAssignment> assignments = orderAssignmentRepository.findByOrderId(orderId);
        for(OrderAssignment assignment : assignments){
            if(assignment.getStatus().equals(AssignmentStatus.PENDING)){
                assignment.setStatus(AssignmentStatus.CANCELED);
                orderAssignmentRepository.update(assignment);
            }
        }
    }

    public PageResponse<RepairOrder> getOrders(User user,int page,int limit){
        PageResponse<RepairOrder> orders = repairOrderRepository.findByUserIdWithPage(user.getUserId(), page, page);
        return orders;
    }
}
