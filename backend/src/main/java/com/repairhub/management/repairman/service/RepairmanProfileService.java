package com.repairhub.management.repairman.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.pulsar.PulsarProperties.Transaction;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalEventPublisher;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.auth.repository.UserRepository;
import com.repairhub.management.order.entity.OrderAssignment;
import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.enums.AssignmentStatus;
import com.repairhub.management.order.event.AssignmentCopedEvent;
import com.repairhub.management.order.repository.OrderAssignmentRepository;
import com.repairhub.management.order.repository.RepairOrderRepository;
import com.repairhub.management.repair.enums.FaultType;
import com.repairhub.management.repairman.entity.RepairmanProfile;
import com.repairhub.management.repairman.repository.RepairmanProfileRepository;

@Service
public class RepairmanProfileService {
    
    private final UserRepository userRepository;
    private final RepairmanProfileRepository repairmanProfileRepository;
    private final OrderAssignmentRepository orderAssignmentRepository;
    private final RepairOrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public RepairmanProfileService(
        UserRepository userRepository,
        RepairmanProfileRepository repairmanProfileRepository,
        OrderAssignmentRepository orderAssignmentRepository,
        RepairOrderRepository orderRepository,
        ApplicationEventPublisher eventPublisher
    ) {
        this.userRepository = userRepository;
        this.repairmanProfileRepository = repairmanProfileRepository;
        this.orderAssignmentRepository = orderAssignmentRepository;
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<RepairmanProfile> findAllWithFilter(FaultType specialty,List<Long> filterIds) {
       List<RepairmanProfile> profiles = null;
        if(specialty == null){
            profiles = repairmanProfileRepository.findAll();
        } else {
            profiles = repairmanProfileRepository.findBySpecialty(specialty);
        }

        if(filterIds != null && !filterIds.isEmpty()){
            profiles.removeIf(profile -> filterIds.contains(profile.getUserId()));
        }

        return profiles;
    }

    @Transactional
    public void copeWithOrderAssignment(OrderAssignment assignment,boolean accepted) {
        RepairmanProfile profile = repairmanProfileRepository.findByUserId(assignment.getRepairmanId())
            .orElseThrow(() -> new RuntimeException("维修工档案不存在"));
        if(accepted){
            assignment.setStatus(AssignmentStatus.ACCEPTED);
        }else{
            assignment.setStatus(AssignmentStatus.REJECTED);
        }
        orderAssignmentRepository.update(assignment);
        completeProfile(profile);
        AssignmentCopedEvent event = new AssignmentCopedEvent(this,profile,assignment);
        eventPublisher.publishEvent(event);
       
    }

    public RepairmanProfile completeProfile(RepairmanProfile profile) {
        List<OrderAssignment> assignments = orderAssignmentRepository.findByRepairmanId(profile.getUserId());
        if (assignments != null && !assignments.isEmpty()) {
            profile.setOrderAssignments(assignments);
        } else {
            profile.setOrderAssignments(List.of());
        }
        return profile;
    }

    public List<RepairOrder> findRepairmanOrders(User repairman){
        Long repairmanId = repairman.getUserId();
        List<OrderAssignment> assignments = orderAssignmentRepository.findByRepairmanId(repairmanId);
        List<OrderAssignment> filteredAssignments = assignments.stream()
        .filter(assignment -> assignment.getStatus().isAccepted())
        .collect(Collectors.toList());
        List<RepairOrder> orders = filteredAssignments.stream()
        .map(assignment -> orderRepository.findById(assignment.getOrderId()).get()).collect(Collectors.toList());
        return orders;
    }

    



    

    

    

    

    

    
}
