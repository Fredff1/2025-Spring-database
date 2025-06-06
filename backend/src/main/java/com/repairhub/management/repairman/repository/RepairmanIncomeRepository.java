package com.repairhub.management.repairman.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.repairhub.management.common.dto.PageResponse;
import com.repairhub.management.repair.entity.LaborFeeLog;

public interface RepairmanIncomeRepository {
    BigDecimal sumTotalIncome(Long repairmanId, LocalDate startTime, LocalDate endTime);

    BigDecimal sumTotalHours(Long repairmanId, LocalDate startTime, LocalDate endTime);

    PageResponse<LaborFeeLog> findFeeLogs(Long repairmanId, LocalDate startTime, LocalDate endTime,int pageNum,int pageSize);
}   

