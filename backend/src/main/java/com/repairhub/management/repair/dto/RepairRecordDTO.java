package com.repairhub.management.repair.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.auth.repository.UserRepository;
import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.repository.RepairOrderRepository;
import com.repairhub.management.repair.entity.RepairRecord;
import com.repairhub.management.repairman.entity.RepairmanProfile;
import com.repairhub.management.repairman.repository.RepairmanProfileRepository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepairRecordDTO {
    private Long recordId;
    private Long orderId;
    private Long repairmanId;
    private String repairmanName;  
    private String faultDescription;  
    private String repairResult;  
    private String status; 
    private BigDecimal actualWorkingHour;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public static RepairRecordDTO from(
        RepairRecord record,
        UserRepository userRepository,
        RepairOrderRepository orderRepository){
        User repairman = userRepository.findById(record.getRepairmanId()).get();
        RepairRecordDTO dto = RepairRecordDTO.builder()
        .recordId(record.getRecordId())
        .orderId(record.getOrderId())
        .repairmanId(record.getRepairmanId())
        .repairmanName(repairman.getUsername())
        .faultDescription(record.getFaultDescription())
        .repairResult(record.getRepairResult())
        .status(record.getOrderStatus().name())
        .createTime(record.getCompletionTime())
        .updateTime(record.getCompletionTime())
        .actualWorkingHour(record.getActualWorkHour())
        .build();
        return dto;
    }
}
