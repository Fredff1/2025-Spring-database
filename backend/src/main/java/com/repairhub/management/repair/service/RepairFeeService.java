package com.repairhub.management.repair.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.repair.dto.CreateLaborFeeLogDTO;
import com.repairhub.management.repair.entity.LaborFeeLog;
import com.repairhub.management.repair.entity.MaterialUsage;
import com.repairhub.management.repair.repository.LaborFeeLogRepository;
import com.repairhub.management.repair.repository.MaterialUsageRepository;

import lombok.Getter;

@Service
@Getter
public class RepairFeeService {
    
    private final LaborFeeLogRepository laborFeeLogRepository;
    private final MaterialUsageRepository materialUsageRepository;

    public RepairFeeService(
        LaborFeeLogRepository laborFeeLogRepository,
        MaterialUsageRepository materialUsageRepository) {
        this.materialUsageRepository = materialUsageRepository;
        this.laborFeeLogRepository = laborFeeLogRepository;
    }

    @Transactional
    public LaborFeeLog createLaborFeeLog(CreateLaborFeeLogDTO dto) {
        LaborFeeLog laborFeeLog = LaborFeeLog.builder()
            .repairmanId(dto.getRepairmanId())
            .orderId(dto.getOrderId())
            .month(LocalDateTime.now().getMonth())
            .totalHours(dto.getTotalHours())
            .totalIncome(dto.getTotalIncome())
            .settleTime(LocalDateTime.now())
            .repairRecordId(dto.getRecordId())
            .build();
        laborFeeLogRepository.insert(laborFeeLog);
        return laborFeeLog;
    }    

    public int updateLaborFeeLog(Long laborFeeLogId,CreateLaborFeeLogDTO updateFeeLogDto) {
        LaborFeeLog laborFeeLog = laborFeeLogRepository.findById(laborFeeLogId)
            .orElseThrow(() -> new IllegalArgumentException("Labor fee log not found"));

        return laborFeeLogRepository.update(laborFeeLog);
    }

    @Transactional
    public int deleteLaborFeeLog(Long laborFeeLogId) {
        return laborFeeLogRepository.delete(laborFeeLogId);
    }

    public BigDecimal calculateFeeByOrder(RepairOrder order){
        BigDecimal fee = BigDecimal.ZERO;
        List<LaborFeeLog> laborFeeLogs = laborFeeLogRepository.findAllByRepairOrderId(order.getOrderId());
        List<MaterialUsage> materialUsages = materialUsageRepository.findByRepairOrderId(order.getOrderId());
        for (LaborFeeLog log : laborFeeLogs) {
            fee = fee.add(log.getTotalIncome());
        }
        for (MaterialUsage usage : materialUsages) {
            fee = fee.add(usage.getUnitPrice().multiply(BigDecimal.valueOf(usage.getQuantity())));
        }
        return fee;
    }

    public List<LaborFeeLog> findFeeLogByRepairmanId(Long repairmanId) {
        return laborFeeLogRepository.findAllByRepairmanId(repairmanId);
    }

    public List<LaborFeeLog> findFeeLogByRepairOrderId(Long repairOrderId) {
        return laborFeeLogRepository.findAllByRepairOrderId(repairOrderId);
    }
}
