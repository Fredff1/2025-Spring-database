package com.repairhub.management.statistic.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CostAnalysisDTO {
    private BigDecimal materialCost;     
    private BigDecimal laborCost;        
    private BigDecimal materialRatio;    // 材料费占比（materialCost / (materialCost + laborCost)）
    private BigDecimal laborRatio;       // 工时费占比（laborCost / (materialCost + laborCost)）
    private String period;      

    public static CostAnalysisDTO from(BigDecimal matCost, BigDecimal labCost, String period) {
        BigDecimal total = matCost.add(labCost);
        BigDecimal matRatio, labRatio;
        if (total.compareTo(BigDecimal.ZERO) == 0) {
            matRatio = BigDecimal.ZERO;
            labRatio = BigDecimal.ZERO;
        } else {
            matRatio = matCost.divide(total,RoundingMode.HALF_UP);
            labRatio = labCost.divide(total,RoundingMode.HALF_UP);
        }
        return CostAnalysisDTO.builder()
                .materialCost(matCost)
                .laborCost(labCost)
                .materialRatio(matRatio)
                .laborRatio(labRatio)
                .period(period)
                .build();
    }
}
