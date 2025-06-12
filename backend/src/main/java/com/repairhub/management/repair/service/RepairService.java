package com.repairhub.management.repair.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalEventPublisher;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.auth.repository.UserRepository;
import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.enums.OrderStatus;
import com.repairhub.management.order.event.OrderDealtEvent;
import com.repairhub.management.order.repository.RepairOrderRepository;
import com.repairhub.management.order.service.OrderService;
import com.repairhub.management.repair.dto.CreateFeedbackAdminResponseDTO;
import com.repairhub.management.repair.dto.CreateLaborFeeLogDTO;
import com.repairhub.management.repair.dto.CreateMaterialUsageDTO;
import com.repairhub.management.repair.dto.CreateRepairFeedbackDTO;
import com.repairhub.management.repair.dto.CreateRepairRecordDTO;
import com.repairhub.management.repair.dto.MaterialUsageDTO;
import com.repairhub.management.repair.dto.RepairFeedbackDTO;
import com.repairhub.management.repair.dto.RepairRecordDTO;
import com.repairhub.management.repair.dto.UpdateRepairRecordDTO;
import com.repairhub.management.repair.entity.LaborFeeLog;
import com.repairhub.management.repair.entity.MaterialUsage;
import com.repairhub.management.repair.entity.RepairFeedback;
import com.repairhub.management.repair.entity.RepairRecord;
import com.repairhub.management.repair.repository.LaborFeeLogRepository;
import com.repairhub.management.repair.repository.MaterialUsageRepository;
import com.repairhub.management.repair.repository.RepairFeedbackRepository;
import com.repairhub.management.repair.repository.RepairRecordRepository;
import com.repairhub.management.repairman.entity.RepairmanProfile;
import com.repairhub.management.repairman.repository.RepairmanProfileRepository;

import lombok.Getter;

@Service
@Getter
public class RepairService {

    private final UserRepository userRepository;
    private final RepairmanProfileRepository profileRepository;
    private final RepairRecordRepository recordRepository;
    private final MaterialUsageRepository materialUsageRepository;
    private final RepairFeedbackRepository feedbackRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final LaborFeeLogRepository laborFeeLogRepository;
    private final OrderService orderService;
    private final RepairFeeService repairFeeService;
    private final ApplicationEventPublisher eventPublisher;


    public RepairService(
        UserRepository userRepository,
        RepairmanProfileRepository profileRepository,
        RepairRecordRepository recordRepository,
        MaterialUsageRepository materialUsageRepository,
        RepairFeedbackRepository feedbackRepository,
        RepairOrderRepository repairOrderRepository,
        LaborFeeLogRepository laborFeeLogRepository,
        OrderService orderService,
        RepairFeeService repairFeeService,
        ApplicationEventPublisher eventPublisher
    ) { 
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.recordRepository = recordRepository;
        this.materialUsageRepository = materialUsageRepository;
        this.feedbackRepository = feedbackRepository;
        this.repairOrderRepository = repairOrderRepository;
        this.laborFeeLogRepository = laborFeeLogRepository;
        this.orderService = orderService;
        this.eventPublisher = eventPublisher;
        this.repairFeeService = repairFeeService;
    }
    
    @Transactional
    public MaterialUsage submitMaterialUsage(CreateMaterialUsageDTO dto){
        MaterialUsage materialUsage = MaterialUsage.builder()
            .orderId(dto.getOrderId())
            .materialName(dto.getMaterialName())
            .quantity(dto.getQuantity())
            .unitPrice(dto.getUnitPrice())
            .createTime(LocalDateTime.now())
            .build();
        materialUsageRepository.insert(materialUsage);
        return materialUsage;
    }

    @Transactional
    public void deleteMaterialUsage(Long materialId){
        MaterialUsage materialUsage = 
            materialUsageRepository.findById(materialId)
                .orElseThrow(() -> new IllegalArgumentException("Material usage not found"));
        materialUsageRepository.delete(materialId);
        RepairOrder order = repairOrderRepository.findById(materialUsage.getOrderId()).get();
        var result = repairFeeService.calculateFeeByOrder(order);
        order.setTotalFee(result);
        repairOrderRepository.update(order);
    }

    @Transactional
    public MaterialUsage updateMaterialUsage(Long materialId,CreateMaterialUsageDTO dto){
        MaterialUsage materialUsage = 
            materialUsageRepository.findById(materialId)
                .orElseThrow(() -> new IllegalArgumentException("Material usage not found"));
        materialUsage.setMaterialName(dto.getMaterialName());
        materialUsage.setQuantity(dto.getQuantity());
        materialUsage.setUnitPrice(dto.getUnitPrice());
        materialUsage.setCreateTime(LocalDateTime.now());
        materialUsageRepository.update(materialUsage);
        RepairOrder order = repairOrderRepository.findById(materialUsage.getOrderId()).get();
        var result = repairFeeService.calculateFeeByOrder(order);
        order.setTotalFee(result);
        repairOrderRepository.update(order);
        return materialUsage;
    }

    @Transactional
    public RepairRecord submitRepairRecord(CreateRepairRecordDTO dto,User repairman){
        
        RepairRecord repairRecord = RepairRecord.builder()
            .orderId(dto.getOrderId())
            .repairmanId(repairman.getUserId())
            .faultDescription(dto.getFaultDescription())
            .repairResult(dto.getRepairResult())
            .completionTime(LocalDateTime.now())
            .actualWorkHour(dto.getActualWorkHour())
            .orderStatus(dto.getStatus())
            .build();
        recordRepository.insert(repairRecord);

        for(CreateMaterialUsageDTO usageDto:dto.getMaterials()){
            MaterialUsage materialUsage = MaterialUsage.builder()
            .orderId(usageDto.getOrderId())
            .materialName(usageDto.getMaterialName())
            .quantity(usageDto.getQuantity())
            .unitPrice(usageDto.getUnitPrice())
            .createTime(LocalDateTime.now())
            .build();
            materialUsageRepository.insert(materialUsage);
        }
        RepairmanProfile profile = profileRepository.findByUserId(repairman.getUserId()).get();
        CreateLaborFeeLogDTO createLaborFeeLogDTO = CreateLaborFeeLogDTO.from
        (dto.getOrderId(), repairman.getUserId(),repairRecord.getRecordId(), dto.getActualWorkHour(), profile.getHourlyMoneyRate());
        

        if(dto.getStatus() != null) {
            repairOrderRepository.findById(dto.getOrderId()).ifPresent(order -> {
                order.setStatus(dto.getStatus());
                order.setUpdateTime(LocalDateTime.now());
                repairOrderRepository.update(order);
                OrderDealtEvent event = new OrderDealtEvent(this, order,createLaborFeeLogDTO,repairRecord);
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
            .feedbackType(dto.getFeedbackType())
            .description(dto.getDescription())
            .feedbackTime(LocalDateTime.now())
            .build();
        feedbackRepository.insert(feedback);
        return feedback;
    }

    @Transactional
    public void insertAdminResponse(CreateFeedbackAdminResponseDTO dto){
        feedbackRepository.insertResponse(dto.getFeedbackId(), dto.getAdminResponse());
    }

    @Transactional
    public void deleteRepairRecord(Long recordId){
        RepairRecord record = recordRepository.findById(recordId)
            .orElseThrow(() -> new IllegalArgumentException("Repair record not found"));
        
        if(record.getOrderStatus() == OrderStatus.COMPLETED) {
            orderService.updateRepairOrderStatus(record.getOrderId(), OrderStatus.PROCESSING);
        }

        recordRepository.delete(recordId);
        LaborFeeLog log = laborFeeLogRepository.findByRecordId(recordId).get();
        laborFeeLogRepository.delete(log.getLaborFeeLogId());

        RepairOrder order = repairOrderRepository.findById(record.getOrderId()).get();
        var result = repairFeeService.calculateFeeByOrder(order);
        order.setTotalFee(result);
        repairOrderRepository.update(order);
        
    }

    @Transactional
    public void updateRepairRecord(Long recordId,UpdateRepairRecordDTO request){
        RepairRecord record = recordRepository.findById(recordId)
            .orElseThrow(() -> new IllegalArgumentException("Repair record not found"));
        record.setActualWorkHour(request.getActualWorkingHours());
        record.setFaultDescription(request.getFaultDescription());
        record.setRepairResult(request.getRepairResult());
        record.setOrderStatus(request.getOrderStatus());
        record.setCompletionTime(LocalDateTime.now());
        recordRepository.update(record);
        if(record.getOrderStatus() == OrderStatus.COMPLETED) {
            orderService.updateRepairOrderStatus(record.getOrderId(), request.getOrderStatus());
        }

        LaborFeeLog log = laborFeeLogRepository.findByRecordId(recordId).get();
        RepairmanProfile profile = profileRepository.findByUserId(log.getRepairmanId()).get();
        log.setSettleTime(LocalDateTime.now());
        log.setTotalHours(request.getActualWorkingHours());
        log.setTotalIncome(request.getActualWorkingHours().multiply(profile.getHourlyMoneyRate()));
        laborFeeLogRepository.update(log);

        RepairOrder order = repairOrderRepository.findById(record.getOrderId()).get();
        var result = repairFeeService.calculateFeeByOrder(order);
        order.setTotalFee(result);
        repairOrderRepository.update(order);
    }

    @Transactional
    public void deleteFeedback(Long feedbackId){
        feedbackRepository.delete(feedbackId);
    }

    public List<RepairRecord> getRepairRecords(Long orderId){
        List<RepairRecord> records = recordRepository.findByRepairOrderId(orderId);
        return records;
    }

    public List<RepairFeedback> getRepairFeedbacks(Long orderId){
        List<RepairFeedback> feedbacks = feedbackRepository.findByRepairOrderId(orderId);
        return feedbacks;
    }

    public List<MaterialUsage> getMaterialUsages(Long orderId){
        List<MaterialUsage> materialUsages = materialUsageRepository.findByRepairOrderId(orderId);
        return materialUsages;
    }

    public RepairRecordDTO convertRecordToDTO(RepairRecord record){
        return RepairRecordDTO.from(record, userRepository, repairOrderRepository);
    }

    public RepairFeedbackDTO convertFeedbackToDTO(RepairFeedback feedback){
        User user = userRepository.findById(feedback.getUserId()).orElse(null);
        return RepairFeedbackDTO.from(feedback,user);
    }

    public MaterialUsageDTO convertMaterialUsageToDTO(MaterialUsage materialUsage){
        return MaterialUsageDTO.from(materialUsage);
    }


}
