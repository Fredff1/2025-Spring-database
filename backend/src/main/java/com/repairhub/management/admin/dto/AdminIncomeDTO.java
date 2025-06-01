package com.repairhub.management.admin.dto;

import java.util.List;

import com.repairhub.management.repair.dto.LaborFeeLogDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminIncomeDTO {
    private List<LaborFeeLogDTO> list;
    private Integer total;
}
