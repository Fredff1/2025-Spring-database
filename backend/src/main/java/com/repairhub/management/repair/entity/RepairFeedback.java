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
public class RepairFeedback {
    private Long feedbackId;
    private Long orderId;
    private Long userId;
    private Long repairmanId;
    private Integer rating;
    private String description;
    private LocalDateTime feedbackTime;
}
