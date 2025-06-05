package com.repairhub.management.statistic.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.repairhub.management.order.enums.OrderStatus;
import com.repairhub.management.repair.enums.FaultType;
import com.repairhub.management.repair.enums.FeedbackType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NegativeFeedbackStatDTO {
    private Long feedbackId;
    private Long orderId;
    private FeedbackType feedbackType;
    private Integer rating;
    private String customerName;
    private FaultType faultType;
    private List<String> repairmanName;
    private OrderStatus orderStatus;
    private String feedbackContent;
    private LocalDateTime feedbackTime;
}
