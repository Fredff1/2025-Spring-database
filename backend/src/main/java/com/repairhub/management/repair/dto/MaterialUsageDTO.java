package com.repairhub.management.repair.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.repairhub.management.repair.entity.MaterialUsage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialUsageDTO {
    private Long usageId;
    private Long orderId;
    private String materialName;  
    private Integer quantity;  
    private BigDecimal unitPrice;  
    private LocalDateTime createTime;

    public static MaterialUsageDTO from(MaterialUsage materialUsage){
        MaterialUsageDTO dto = MaterialUsageDTO.builder()
        .createTime(materialUsage.getCreateTime())
        .unitPrice(materialUsage.getUnitPrice())
        .quantity(materialUsage.getQuantity())
        .materialName(materialUsage.getMaterialName())
        .orderId(materialUsage.getOrderId())
        .usageId(materialUsage.getMaterialId())
        .build();
        return dto;
    }
}
