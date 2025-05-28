package com.repairhub.management.auth.service;

import java.util.List;

import org.springframework.boot.autoconfigure.pulsar.PulsarProperties.Transaction;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalEventPublisher;

import com.repairhub.management.auth.entity.RepairmanProfile;
import com.repairhub.management.auth.repository.RepairmanProfileRepository;
import com.repairhub.management.auth.repository.UserRepository;
import com.repairhub.management.order.entity.OrderAssignment;
import com.repairhub.management.order.event.AssignmentCopedEvent;
import com.repairhub.management.order.repository.OrderAssignmentRepository;
import com.repairhub.management.repair.enums.FaultType;

@Service
public class RepairmanProfileService {
    
    private final UserRepository userRepository;
    private final RepairmanProfileRepository repairmanProfileRepository;
    private final OrderAssignmentRepository orderAssignmentRepository;
    private final ApplicationEventPublisher eventPublisher;

    public RepairmanProfileService(
        UserRepository userRepository,
        RepairmanProfileRepository repairmanProfileRepository,
        OrderAssignmentRepository orderAssignmentRepository,
        ApplicationEventPublisher eventPublisher
    ) {
        this.userRepository = userRepository;
        this.repairmanProfileRepository = repairmanProfileRepository;
        this.orderAssignmentRepository = orderAssignmentRepository;
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
        assignment.setAccepted(accepted);
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

    



    

    

    

    

    

    
}
