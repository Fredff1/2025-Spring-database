package com.repairhub.management.repair.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateFeedbackAdminResponseDTO {
    private Long feedbackId;
    private String adminResponse;
}
