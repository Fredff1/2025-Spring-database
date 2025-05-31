package com.repairhub.management.repairman.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.repairhub.management.repair.entity.LaborFeeLog;

public interface RepairmanIncomeRepository {
    BigDecimal sumTotalIncome(Long repairmanId, LocalDate startTime, LocalDate endTime);

    BigDecimal sumTotalHours(Long repairmanId, LocalDate startTime, LocalDate endTime);

    List<LaborFeeLog> findFeeLogs(Long repairmanId, LocalDate startTime, LocalDate endTime);
}   

