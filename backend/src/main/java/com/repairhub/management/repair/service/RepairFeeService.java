package com.repairhub.management.repair.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.repairhub.management.repair.dto.CreateLaborFeeLogDTO;
import com.repairhub.management.repair.entity.LaborFeeLog;
import com.repairhub.management.repair.repository.LaborFeeLogRepository;

@Service
public class RepairFeeService {
    
    private final LaborFeeLogRepository laborFeeLogRepository;

    public RepairFeeService(LaborFeeLogRepository laborFeeLogRepository) {
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

    public List<LaborFeeLog> findFeeLogByRepairmanId(Long repairmanId) {
        return laborFeeLogRepository.findAllByRepairmanId(repairmanId);
    }
}
