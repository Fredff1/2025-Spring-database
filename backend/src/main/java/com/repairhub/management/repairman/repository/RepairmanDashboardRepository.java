package com.repairhub.management.repairman.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface RepairmanDashboardRepository {
    // ——— 获取“概览”所需的 4 个指标 ———
    int countTotalAssignments(Long repairmanId, LocalDateTime startOfToday, LocalDateTime now);
    int countProcessingOrders(Long repairmanId);
    BigDecimal sumMonthlyIncome(Long repairmanId, LocalDateTime startOfMonth, LocalDateTime now);
    Double averageRatingAllTime(Long repairmanId);

    // ——— 获取“统计”所需的 4 个指标 ———
    int countCompletedOrdersAfter(Long repairmanId, LocalDateTime startTime);
    BigDecimal sumWorkHoursAfter(Long repairmanId, LocalDateTime startTime);
    BigDecimal sumIncomeAfter(Long repairmanId, LocalDateTime startTime);
    Double averageRatingAfter(Long repairmanId, LocalDateTime startTime);
}
