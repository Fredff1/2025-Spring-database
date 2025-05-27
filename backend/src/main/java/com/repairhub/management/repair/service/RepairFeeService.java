package com.repairhub.management.repair.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.repair.dto.CreateLaborFeeLogDTO;
import com.repairhub.management.repair.entity.LaborFeeLog;
import com.repairhub.management.repair.entity.MaterialUsage;
import com.repairhub.management.repair.repository.LaborFeeLogRepository;
import com.repairhub.management.repair.repository.MaterialUsageRepository;

@Service
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
            .month(dto.getMonth())
            .totalHours(dto.getTotalHours())
            .totalIncome(dto.getTotalIncome())
            .settleTime(LocalDateTime.now())
            .build();
        laborFeeLogRepository.insert(laborFeeLog);
        return laborFeeLog;
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
