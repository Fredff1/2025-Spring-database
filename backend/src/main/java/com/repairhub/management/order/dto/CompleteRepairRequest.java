package com.repairhub.management.order.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompleteRepairRequest {
    private String repairDetails;
    private List<String> partsUsed;
    private Integer laborHours;
}
