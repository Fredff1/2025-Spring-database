package com.repairhub.management.repair.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.repair.entity.LaborFeeLog;
import com.repairhub.management.repairman.entity.RepairmanProfile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LaborFeeLogDTO {
    private Long LaborFeeLogId;
    private Long orderId;
    private Long repairmanId;
    private String repairmanName;
    private Month month;
    private BigDecimal hourlyMoneyRate;
    private BigDecimal totalHours;
    private BigDecimal totalIncome;
    private LocalDateTime settleTime;

    public static LaborFeeLogDTO from(LaborFeeLog log,RepairmanProfile repairmanProfile,User repairman){
        LaborFeeLogDTO dto = LaborFeeLogDTO.builder()
                .LaborFeeLogId(log.getLaborFeeLogId())
                .orderId(log.getOrderId())
                .repairmanName(repairman.getUsername())
                .repairmanId(log.getRepairmanId())
                .month(log.getMonth())
                .hourlyMoneyRate(repairmanProfile.getHourlyMoneyRate())
                .totalHours(log.getTotalHours())
                .totalIncome(log.getTotalIncome())
                .settleTime(log.getSettleTime())
                .build();
        return dto;
    }

}
