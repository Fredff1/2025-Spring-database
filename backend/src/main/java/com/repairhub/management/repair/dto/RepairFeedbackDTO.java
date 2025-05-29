package com.repairhub.management.repair.dto;

import java.time.LocalDateTime;

import com.repairhub.management.repair.entity.RepairFeedback;
import com.repairhub.management.repair.enums.FeedbackType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepairFeedbackDTO {
    private Long feedbackId;
    private Long orderId;
    private FeedbackType type;  // 反馈类型：RATING(评分)、URGENT(催单)、GENERAL(一般反馈)
    private Integer rating;  // 评分，仅当type为RATING时有值
    private String description;  // 反馈内容
    private LocalDateTime feedbackTime;

    public static RepairFeedbackDTO from(RepairFeedback feedback){
        RepairFeedbackDTO dto = RepairFeedbackDTO.builder()
        .feedbackId(feedback.getFeedbackId())
        .orderId(feedback.getOrderId())
        .type(feedback.getFeedbackType())
        .rating(feedback.getRating())
        .description(feedback.getDescription())
        .feedbackTime(feedback.getFeedbackTime())
        .build();
        return dto;
    }
}
