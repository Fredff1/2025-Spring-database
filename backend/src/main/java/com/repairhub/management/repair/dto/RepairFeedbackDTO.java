package com.repairhub.management.repair.dto;

import java.time.LocalDateTime;

import com.repairhub.management.auth.entity.User;
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
    private String username;

    public static RepairFeedbackDTO from(
        RepairFeedback feedback,
        User user){
        Integer rating = null;
        if (feedback.getFeedbackType() == FeedbackType.RATING) {
            rating = feedback.getRating();
        }
        RepairFeedbackDTO dto = RepairFeedbackDTO.builder()
        .feedbackId(feedback.getFeedbackId())
        .orderId(feedback.getOrderId())
        .type(feedback.getFeedbackType())
        .rating(rating)
        .description(feedback.getDescription())
        .feedbackTime(feedback.getFeedbackTime())
        .username(user.getUsername())
        .build();
        return dto;
    }
}
