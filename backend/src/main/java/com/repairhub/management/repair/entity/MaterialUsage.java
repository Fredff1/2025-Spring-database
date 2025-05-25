package com.repairhub.management.repair.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialUsage {
    private Long materialId;
    private Long orderId;
    private String materialName;
    private Integer quantity;
    private BigDecimal unitPrice;
}
