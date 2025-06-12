package com.repairhub.management.repair.entity;

import java.time.LocalDateTime;

import com.repairhub.management.repair.enums.FeedbackType;

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
    private Integer rating;
    private FeedbackType feedbackType;
    private String description;
    private LocalDateTime feedbackTime;
    private String response;
}
