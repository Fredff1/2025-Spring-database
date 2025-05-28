package com.repairhub.management.repair.service;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalEventPublisher;

import com.repairhub.management.order.event.OrderCompletedEvent;
import com.repairhub.management.order.repository.RepairOrderRepository;
import com.repairhub.management.repair.dto.CreateMaterialUsageDTO;
import com.repairhub.management.repair.dto.CreateRepairFeedbackDTO;
import com.repairhub.management.repair.dto.CreateRepairRecordDTO;
import com.repairhub.management.repair.entity.MaterialUsage;
import com.repairhub.management.repair.entity.RepairFeedback;
import com.repairhub.management.repair.entity.RepairRecord;
import com.repairhub.management.repair.repository.MaterialUsageRepository;
import com.repairhub.management.repair.repository.RepairFeedbackRepository;
import com.repairhub.management.repair.repository.RepairRecordRepository;

@Service
public class RepairService {

    private final RepairRecordRepository recordRepository;
    private final MaterialUsageRepository materialUsageRepository;
    private final RepairFeedbackRepository feedbackRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final ApplicationEventPublisher eventPublisher;


    public RepairService(
        RepairRecordRepository recordRepository,
        MaterialUsageRepository materialUsageRepository,
        RepairFeedbackRepository feedbackRepository,
        RepairOrderRepository repairOrderRepository,
        ApplicationEventPublisher eventPublisher
    ) {
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
            repairOrderRepository.findById(dto.getOrderId()).ifPresent(Order -> {
                Order.setStatus(dto.getOrderStatus());
                repairOrderRepository.update(Order);
                OrderCompletedEvent event = new OrderCompletedEvent(this, Order);
                eventPublisher.publishEvent(event);
            });
        }
        return repairRecord;
    }

    @Transactional
    public RepairFeedback submitRepairFeedback(CreateRepairFeedbackDTO dto){
        RepairFeedback feedback = RepairFeedback.builder()
            .orderId(dto.getOrderId())
            .userId(dto.getUserId())
            .repairmanId(dto.getRepairmanId())
            .rating(dto.getRating())
            .description(dto.getDescription())
            .feedbackTime(LocalDateTime.now())
            .build();
        feedbackRepository.insert(feedback);
        return feedback;
    }


}
