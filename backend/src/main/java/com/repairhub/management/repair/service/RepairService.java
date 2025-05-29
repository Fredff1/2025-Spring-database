package com.repairhub.management.repair.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalEventPublisher;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.auth.repository.UserRepository;
import com.repairhub.management.order.event.OrderCompletedEvent;
import com.repairhub.management.order.repository.RepairOrderRepository;
import com.repairhub.management.repair.dto.CreateMaterialUsageDTO;
import com.repairhub.management.repair.dto.CreateRepairFeedbackDTO;
import com.repairhub.management.repair.dto.CreateRepairRecordDTO;
import com.repairhub.management.repair.dto.MaterialUsageDTO;
import com.repairhub.management.repair.dto.RepairFeedbackDTO;
import com.repairhub.management.repair.dto.RepairRecordDTO;
import com.repairhub.management.repair.entity.MaterialUsage;
import com.repairhub.management.repair.entity.RepairFeedback;
import com.repairhub.management.repair.entity.RepairRecord;
import com.repairhub.management.repair.repository.MaterialUsageRepository;
import com.repairhub.management.repair.repository.RepairFeedbackRepository;
import com.repairhub.management.repair.repository.RepairRecordRepository;

@Service
public class RepairService {

    private final UserRepository userRepository;
    private final RepairRecordRepository recordRepository;
    private final MaterialUsageRepository materialUsageRepository;
    private final RepairFeedbackRepository feedbackRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final ApplicationEventPublisher eventPublisher;


    public RepairService(
        UserRepository userRepository,
        RepairRecordRepository recordRepository,
        MaterialUsageRepository materialUsageRepository,
        RepairFeedbackRepository feedbackRepository,
        RepairOrderRepository repairOrderRepository,
        ApplicationEventPublisher eventPublisher
    ) { 
        this.userRepository = userRepository;
        this.recordRepository = recordRepository;
        this.materialUsageRepository = materialUsageRepository;
        this.feedbackRepository = feedbackRepository;
        this.repairOrderRepository = repairOrderRepository;
        this.eventPublisher = eventPublisher;
    }
    
    @Transactional
    public MaterialUsage submitMaterialUsage(CreateMaterialUsageDTO dto){
        MaterialUsage materialUsage = MaterialUsage.builder()
            .orderId(dto.getOrderId())
            .materialName(dto.getMaterialName())
            .quantity(dto.getQuantity())
            .unitPrice(dto.getUnitPrice())
            .build();
        materialUsageRepository.insert(materialUsage);
        return materialUsage;
    }

    @Transactional
    public RepairRecord submitRepairRecord(CreateRepairRecordDTO dto){
        RepairRecord repairRecord = RepairRecord.builder()
            .orderId(dto.getOrderId())
            .faultDescription(dto.getFaultDescription())
            .repairResult(dto.getRepairResult())
            .completionTime(LocalDateTime.now())
            .build();
        recordRepository.insert(repairRecord);
        if(dto.getOrderStatus() != null) {
            repairOrderRepository.findById(dto.getOrderId()).ifPresent(order -> {
                order.setStatus(dto.getOrderStatus());
                order.setUpdateTime(LocalDateTime.now());
                repairOrderRepository.update(order);
                OrderCompletedEvent event = new OrderCompletedEvent(this, order);
                eventPublisher.publishEvent(event);
            });
        }
        return repairRecord;
    }

    @Transactional
    public RepairFeedback submitRepairFeedback(CreateRepairFeedbackDTO dto,User user){
        RepairFeedback feedback = RepairFeedback.builder()
            .orderId(dto.getOrderId())
            .userId(user.getUserId())
            .rating(dto.getRating())
            .description(dto.getDescription())
            .feedbackTime(LocalDateTime.now())
            .build();
        feedbackRepository.insert(feedback);
        return feedback;
    }

    public List<RepairRecord> getRepairRecords(Long orderId){
        List<RepairRecord> records = recordRepository.findByRepairOrderId(orderId);
        return records;
    }

    public List<RepairFeedback> getRepairFeedbacks(Long orderId){
        List<RepairFeedback> feedbacks = feedbackRepository.findByRepairOrderId(orderId);
        return feedbacks;
    }

    public RepairRecordDTO convertRecordToDTO(RepairRecord record){
        return RepairRecordDTO.from(record, userRepository, repairOrderRepository);
    }

    public RepairFeedbackDTO convertFeedbackToDTO(RepairFeedback feedback){
        return RepairFeedbackDTO.from(feedback);
    }

    public MaterialUsageDTO convertMaterialUsageToDTO(MaterialUsage materialUsage){
        return MaterialUsageDTO.from(materialUsage);
    }


}
