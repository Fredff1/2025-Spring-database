package com.repairhub.management.repair.dto;

import org.springframework.data.relational.core.sql.In;

import com.repairhub.management.repair.enums.FeedbackType;

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
    private FeedbackType feedbackType;
    private Integer rating;
    private String description;
}
