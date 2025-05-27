package com.repairhub.management.repair.dto;

import org.springframework.data.relational.core.sql.In;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateRepairFeedbackDTO {
    private Long orderId;
    private Long userId;
    private Long repairmanId;
    private Integer rating;
    private String description;
}
