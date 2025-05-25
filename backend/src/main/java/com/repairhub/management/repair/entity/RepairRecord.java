package com.repairhub.management.repair.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepairRecord {
    private Long recordId;
    private Long orderId;
    private String faultDescription;
    private String repairResult;
    private LocalDateTime completionTime;
}
