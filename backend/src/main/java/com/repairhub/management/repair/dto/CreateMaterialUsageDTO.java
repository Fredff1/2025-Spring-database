package com.repairhub.management.repair.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateMaterialUsageDTO {
    private Long orderId;
    private String materialName;
    private Integer quantity;
    private BigDecimal unitPrice;
}
